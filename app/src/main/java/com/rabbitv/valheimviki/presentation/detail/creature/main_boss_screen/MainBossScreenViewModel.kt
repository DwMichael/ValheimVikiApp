package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen

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
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
    private val mainBossId: String = checkNotNull(savedStateHandle[Constants.MAIN_BOSS_ARGUMENT_KEY])

    private val _mainBoss = MutableStateFlow<MainBoss?>(null)
    val mainBoss: StateFlow<MainBoss?> = _mainBoss

    private val _relatedForsakenAltar = MutableStateFlow<PointOfInterest?>(null)
    val relatedForsakenAltar: StateFlow<PointOfInterest?> = _relatedForsakenAltar

    private val _sacrificialStones = MutableStateFlow<PointOfInterest?>(null)
    val sacrificialStones: StateFlow<PointOfInterest?> = _sacrificialStones

    private val _dropItems = MutableStateFlow<List<Material?>>(emptyList())
    val dropItems: StateFlow<List<Material?>> = _dropItems

    private val _relatedSummoningItems = MutableStateFlow<List<Material?>>(emptyList())
    val relatedSummoningItems: StateFlow<List<Material?>> = _relatedSummoningItems

    private val _relatedBiome = MutableStateFlow<Biome?>(null)
    val relatedBiome: StateFlow<Biome?> = _relatedBiome

    private val _trophy = MutableStateFlow<Material?>(null)
    val trophy: StateFlow<Material?> = _trophy


    init {
        launch()
    }


    fun launch() {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(mainBossId).let {
                    _mainBoss.value = CreatureFactory.createFromCreature(it)
                }
                val relatedObjects: List<RelatedItem> = async {
                    relationUseCases.getRelatedIdsUseCase(mainBossId)
                }.await()
                val relatedIds = relatedObjects.map { it.id }
                val deferreds = listOf(
                    async {
                        val pointOfInterest =
                            pointOfInterestUseCases.getPointsOfInterestBySubCategoryUseCase(
                                PointOfInterestSubCategory.FORSAKEN_ALTAR
                            )
                        val poi = pointOfInterestUseCases.getPointsOfInterestBySubCategoryUseCase(
                            PointOfInterestSubCategory.STRUCTURE
                        )
                        _sacrificialStones.value = poi.find {
                            it.imageUrl == "https://static.wikia.nocookie.net/valheim/images/2/29/Sarcrifial_Stones.jpg/revision/latest?cb=20230416093844"
                        }

                        _relatedForsakenAltar.value = pointOfInterest.find {
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

                        val relatedAltarOfferings  = materialUseCases.getMaterialsBySubCategory(
                            subCategory = MaterialSubCategory.FORSAKEN_ALTAR_OFFERING,
                        ).filter { it.id in relatedIds }

                        val relevantCreatureDrops = materialUseCases.getMaterialsBySubCategory(
                            MaterialSubCategory.CREATURE_DROP)
                            .filter { it.id in relatedIds }
                        val allRelevantDrops = relatedAltarOfferings + relevantCreatureDrops
                        _relatedSummoningItems.value  = allRelevantDrops.distinctBy { it.id}


                        },
                    async {

                        val materials = materialUseCases.getMaterialsBySubCategory(
                            MaterialSubCategory.BOSS_DROP
                        )
                        _dropItems.value = materials.filter { material ->
                            material.id in relatedIds
                        }

                        _trophy.value =materials.filter { material ->
                            (material.id in relatedIds)
                        }.find {
                            it.subType == MaterialSubType.TROPHY.toString()
                        }

                    }
                )

                deferreds.awaitAll()

            }


        } catch (e: Exception) {
            Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
        }
    }
}