package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.MaterialsFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MiniBossDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val creatureUseCases: CreatureUseCases,
    private val pointOfInterestUseCases: PointOfInterestUseCases,
    private val materialUseCases: MaterialUseCases,
    private val relationUseCases: RelationUseCases
) : ViewModel() {
    private val miniBossId: String =
        checkNotNull(savedStateHandle[Constants.MINI_BOSS_ARGUMENT_KEY])
    private val _miniBoss = MutableStateFlow<MiniBoss?>(null)
    private val _primarySpawn = MutableStateFlow<PointOfInterest?>(null)
    private val _dropItems = MutableStateFlow<List<Material>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _miniBoss,
        _primarySpawn,
        _dropItems,
        _isLoading,
        _error,
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MiniBossDetailUiState(
            miniBoss = values[0] as MiniBoss?,
            primarySpawn = values[1] as PointOfInterest?,
            dropItems = values[2] as List<Material>,
            isLoading = values[3] as Boolean,
            error = values[4] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = MiniBossDetailUiState()
    )

    init {
        launch()
    }


    fun launch() {

        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(miniBossId).let {
                    _miniBoss.value = CreatureFactory.createFromCreature(it)
                }
                val relatedObjects: List<RelatedItem> = async {
                    relationUseCases.getRelatedIdsUseCase(miniBossId)
                }.await()

                val relatedIds = relatedObjects.map { it.id }

                val deferreds = listOf(
                    async {
                        try {
                            val pointOfInterest =
                                pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(relatedIds)
                            _primarySpawn.value = pointOfInterest.find {
                                it.id in relatedIds
                            }
                        } catch (e: PointOfInterestByIdsFetchLocalException) {
                            _primarySpawn.value = null
                        } catch (e: Exception) {
                            throw e
                        }

                    },
                    async {
                        try {
                            val materials = materialUseCases.getMaterialsBySubCategory(
                                MaterialSubCategory.MINI_BOSS_DROP
                            )
                            _dropItems.value = materials.filter { material ->
                                material.id in relatedIds
                            }
                        } catch (e: MaterialsFetchLocalException) {
                            _dropItems.value = emptyList()
                        }
                        //TODO: WHEN ADD WEAPON DATA MAKE SURE TO FETCH WEAPON AS DROP ITEM FOR LORD RETO
                    }
                )
                deferreds.awaitAll()
            }
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
            _isLoading.value = false
            _error.value = e.message
        }
    }
}