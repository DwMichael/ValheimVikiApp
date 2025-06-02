package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.components.DropItem
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AggressiveCreatureDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val creatureUseCases: CreatureUseCases,
    private val relationUseCases: RelationUseCases,
    private val biomeUseCases: BiomeUseCases,
    private val materialUseCases: MaterialUseCases,
) : ViewModel() {
    private val _aggressiveCreatureId: String =
        checkNotNull(savedStateHandle[Constants.AGGRESSIVE_CREATURE_KEY])
    private val _creature = MutableStateFlow<AggressiveCreature?>(null)
    private val _biome = MutableStateFlow<Biome?>(null)
    private val _dropItems = MutableStateFlow<List<DropItem>>(emptyList())
    private val _isLoading = MutableStateFlow<Boolean>(false)
    private val _error = MutableStateFlow<String?>(null)


    val uiState = combine(
        _creature,
        _biome,
        _dropItems,
        _isLoading,
        _error,
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        (AggressiveCreatureDetailUiState(
            aggressiveCreature = values[0] as AggressiveCreature?,
            biome = values[1] as Biome?,
            dropItems = values[2] as List<DropItem>,
            isLoading = values[3] as Boolean,
            error = values[4] as String?
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = AggressiveCreatureDetailUiState()
    )

    init {
        initializeCreatureData()
    }


    internal fun initializeCreatureData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {

                val creatureData = creatureUseCases.getCreatureById(_aggressiveCreatureId).first()
                _creature.value = CreatureFactory.createFromCreature(creatureData)


                val relatedObjects: List<RelatedItem> =
                    relationUseCases.getRelatedIdsUseCase(_aggressiveCreatureId).first()
                val relatedIds = relatedObjects.map { it.id }


                val deferredBiome = async {
                    val biomesList = biomeUseCases.getLocalBiomesUseCase().first()
                    _biome.value = biomesList.find { it.id in relatedIds }
                }

                val deferredMaterials = async {
                    try {
                        if (relatedIds.isNotEmpty()) {
                            val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
                            val tempList = mutableListOf<DropItem>()
                            val relatedItemsMap = relatedObjects.associateBy { it.id }
                            materials.forEach { material ->
                                val relatedItem = relatedItemsMap[material.id]
                                val quantityList = listOf(
                                    relatedItem?.quantity,
                                    relatedItem?.quantity2star,
                                    relatedItem?.quantity3star
                                )
                                val chanceStarList = listOf(
                                    relatedItem?.chance1star,
                                    relatedItem?.chance2star,
                                    relatedItem?.chance3star
                                )
                                tempList.add(
                                    DropItem(
                                        material = material,
                                        quantityList = quantityList,
                                        chanceStarList = chanceStarList,
                                    )
                                )
                            }
                            _dropItems.value = tempList
                            Log.d("DROP ITEMS ", "$tempList")
                        } else {
                            _dropItems.value = emptyList()
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "AggressiveCreatureDetail ViewModel",
                            "Error fetching materials: $e",
                            e
                        )
                        _dropItems.value = emptyList()
                    }
                }

                awaitAll(deferredBiome, deferredMaterials)

            } catch (e: Exception) {
                Log.e(
                    "General fetch error AggressiveDetailViewModel",
                    "Error in initializeCreatureData: ${e.message}",
                    e
                )
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}