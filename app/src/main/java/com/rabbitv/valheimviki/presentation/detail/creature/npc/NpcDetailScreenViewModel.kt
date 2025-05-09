package com.rabbitv.valheimviki.presentation.detail.creature.npc

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
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
class NpcDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val creatureUseCases: CreatureUseCases,
    private val relationUseCases: RelationUseCases,
    private val biomeUseCases: BiomeUseCases,
    private val materialUseCases: MaterialUseCases,
) : ViewModel() {
    private val _npcId: String =
        checkNotNull(savedStateHandle[Constants.PASSIVE_CREATURE_KEY])
    private val _creature = MutableStateFlow<NPC?>(null)
    private val _biome = MutableStateFlow<Biome?>(null)
    private val _shopItems = MutableStateFlow<List<Material>>(emptyList())
    private val _shopSellItems = MutableStateFlow<List<Material>>(emptyList())
    private val _isLoading = MutableStateFlow<Boolean>(false)
    private val _error = MutableStateFlow<String?>(null)


    val uiState = combine(
        _creature,
        _biome,
        _shopItems,
        _shopSellItems,
        _isLoading,
        _error,
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        NpcDetailUiState(
            npc = values[0] as NPC?,
            biome = values[1] as Biome?,
            shopItems = values[2] as List<Material>,
            shopSellItems = values[3] as List<Material>,
            isLoading = values[4] as Boolean,
            error = values[5] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NpcDetailUiState()
    )

    init {
        launch()
    }


    internal fun launch() {
        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(_npcId).let {
                    _creature.value = CreatureFactory.createFromCreature(it)
                }
                val relatedObjects: List<RelatedItem> = async {
                    relationUseCases.getRelatedIdsUseCase(_npcId)
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
                            val materials =
                                materialUseCases.getMaterialsBySubCategory(MaterialSubCategory.SHOP)
                                    .filter {
                                        it.id in relatedIds
                                    }
                            _shopItems.value = materials
                        } catch (e: Exception) {
                            Log.e("PassiveCreatureDetail ViewModel", "$e")
                            _shopItems.value = emptyList()
                        }
                    },
                    async {
                        try {
                            val materials =
                                materialUseCases.getMaterialsBySubCategory(MaterialSubCategory.VALUABLE)
                                    .filter {
                                        it.id in relatedIds
                                    }
                            _shopSellItems.value = materials
                        } catch (e: Exception) {
                            Log.e("PassiveCreatureDetail ViewModel", "$e")
                            _shopSellItems.value = emptyList()
                        }
                    },
                )
                deferreds.awaitAll()
            }
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("General fetch error PassiveDetailViewModel", e.message.toString())
            _isLoading.value = false
            _error.value = e.message
        }
    }
}