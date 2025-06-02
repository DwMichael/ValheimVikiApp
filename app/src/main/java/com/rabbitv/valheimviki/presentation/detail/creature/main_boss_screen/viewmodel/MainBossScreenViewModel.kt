package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossDetailUiState
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainBossScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val creatureUseCases: CreatureUseCases,
    private val pointOfInterestUseCases: PointOfInterestUseCases,
    private val materialUseCases: MaterialUseCases,
    private val biomeUseCases: BiomeUseCases,
    private val relationUseCases: RelationUseCases
) : ViewModel() {
    private val mainBossId: String =
        checkNotNull(savedStateHandle[Constants.MAIN_BOSS_ARGUMENT_KEY])
    private val _mainBoss = MutableStateFlow<MainBoss?>(null)
    private val _relatedForsakenAltar = MutableStateFlow<PointOfInterest?>(null)
    private val _sacrificialStones = MutableStateFlow<PointOfInterest?>(null)
    private val _dropItems = MutableStateFlow<List<Material>>(emptyList())
    private val _relatedSummoningItems = MutableStateFlow<List<Material>>(emptyList())
    private val _relatedBiome = MutableStateFlow<Biome?>(null)
    private val _trophy = MutableStateFlow<Material?>(null)

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)


    val uiState = combine(
        _mainBoss,
        _relatedForsakenAltar,
        _sacrificialStones,
        _dropItems,
        _relatedSummoningItems,
        _relatedBiome,
        _trophy,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        (MainBossDetailUiState(
            mainBoss = values[0] as MainBoss?,
            relatedForsakenAltar = values[1] as PointOfInterest?,
            sacrificialStones = values[2] as PointOfInterest?,
            dropItems = values[3] as List<Material>,
            relatedSummoningItems = values[4] as List<Material>,
            relatedBiome = values[5] as Biome?,
            trophy = values[6] as Material?,
            isLoading = values[7] as Boolean,
            error = values[8] as String?
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = MainBossDetailUiState()
    )


    init {
        launch()
    }


    fun launch() {

        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                _mainBoss.value = CreatureFactory.createFromCreature(
                    creatureUseCases.getCreatureById(mainBossId).first()
                )


                val relatedIds: List<String> = async {
                    relationUseCases.getRelatedIdsUseCase(mainBossId)
                        .first()
                        .map { it.id }
                }.await()

                val deferreds = listOf(
                    async {
                        val pointOfInterestList = pointOfInterestUseCases
                            .getPointsOfInterestBySubCategoryUseCase(PointOfInterestSubCategory.FORSAKEN_ALTAR)
                            .first()

                        val poiList = pointOfInterestUseCases
                            .getPointsOfInterestBySubCategoryUseCase(PointOfInterestSubCategory.STRUCTURE)
                            .first()

                        _sacrificialStones.value = poiList.find {
                            it.imageUrl == "https://static.wikia.nocookie.net/valheim/images/2/29/Sarcrifial_Stones.jpg/revision/latest?cb=20230416093844"
                        }

                        _relatedForsakenAltar.value = pointOfInterestList.find {
                            it.id in relatedIds
                        }


                    },
                    async {
                        val biome = biomeUseCases.getLocalBiomesUseCase().first()
                        _relatedBiome.value = biome.find {
                            it.id in relatedIds
                        }
                    },
                    async {

                        val relatedAltarOfferings = materialUseCases.getMaterialsBySubCategory(
                            subCategory = MaterialSubCategory.FORSAKEN_ALTAR_OFFERING,
                        ).first().filter { it.id in relatedIds }

                        val relevantCreatureDrops = materialUseCases.getMaterialsBySubCategory(
                            MaterialSubCategory.CREATURE_DROP
                        ).first().filter { it.id in relatedIds }

                        val allRelevantDrops = relatedAltarOfferings + relevantCreatureDrops
                        _relatedSummoningItems.value = allRelevantDrops.distinctBy { it.id }


                    },
                    async {

                        val materials = materialUseCases.getMaterialsBySubCategory(
                            MaterialSubCategory.BOSS_DROP
                        ).first()
                        _dropItems.value = materials.filter { material ->
                            material.id in relatedIds
                        }

                        _trophy.value = materials.filter { material ->
                            (material.id in relatedIds)
                        }.find {
                            it.subType == MaterialSubType.TROPHY.toString()
                        }

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