package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

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
        AggressiveCreatureDetailUiState(
            aggressiveCreature = values[0] as AggressiveCreature?,
            biome = values[1] as Biome?,
            dropItems = values[2] as List<DropItem>,
            isLoading = values[3] as Boolean,
            error = values[4] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AggressiveCreatureDetailUiState()
    )

    init {
        launch()
    }


    internal fun launch() {
        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(_aggressiveCreatureId).let {
                    _creature.value = CreatureFactory.createFromCreature(it)
                }
                val relatedObjects: List<RelatedItem> = async {
                    relationUseCases.getRelatedIdsUseCase(_aggressiveCreatureId)
                }.await()
                val relatedIds = relatedObjects.map { it.id }

                val deferreds = listOf(
                    async {
                        val biome = biomeUseCases.getLocalBiomesUseCase().first()
                        _biome.value = biome.find {
                            it.id in relatedIds
                        }
                    },
                    async {
                        try {
                            val materials = materialUseCases.getMaterialsByIds(relatedIds)
                            val tempList = mutableListOf<DropItem>()

                            val relatedItemsMap = relatedObjects.associateBy { it.id }
                            for (material in materials) {
                                val relatedItem = relatedItemsMap[material.id]
                                val quantityList = listOf<Int?>(
                                    relatedItem?.quantity,
                                    relatedItem?.quantity2star,
                                    relatedItem?.quantity3star
                                )
                                val chanceStarList = listOf<Int?>(
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
                            Log.e("DROP ITEMS ", "$tempList")
                        } catch (e: Exception) {
                            Log.e("AggressiveCreatureDetail ViewModel", "$e")
                            _dropItems.value = emptyList()
                        }

                    },

                    )
                deferreds.awaitAll()
            }
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("General fetch error AggressiveDetailViewModel", e.message.toString())
            _isLoading.value = false
            _error.value = e.message
        }
    }
}