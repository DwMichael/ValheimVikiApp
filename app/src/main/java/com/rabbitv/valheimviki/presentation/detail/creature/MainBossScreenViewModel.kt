package com.rabbitv.valheimviki.presentation.detail.creature

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory.createFromCreature
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
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
    private val mainBossId: String = checkNotNull(savedStateHandle[MAIN_BOSS_ARGUMENT_KEY])

    private val _mainBoss = MutableStateFlow<MainBoss?>(null)
    val mainBoss: StateFlow<MainBoss?> = _mainBoss

    private val _relatedForsakenAltar = MutableStateFlow<PointOfInterest?>(null)
    val relatedForsakenAltar: StateFlow<PointOfInterest?> = _relatedForsakenAltar


    private val _dropItems = MutableStateFlow< List<Material>>(emptyList())
    val dropItems: StateFlow< List<Material>> = _dropItems

    private val _relatedSummoningItems = MutableStateFlow< List<Material?>>(emptyList())
    val relatedSummoningItems: StateFlow< List<Material?>> = _relatedSummoningItems

    private val _relatedBiome = MutableStateFlow<Biome?>(null)
    val relatedBiome: StateFlow<Biome?> = _relatedBiome

    init {
        launch()
    }


    fun launch() {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(mainBossId).let {
                    _mainBoss.value = createFromCreature(it)
                }
                val relatedObjects: List<String> = async {
                    relationUseCases.getRelatedIdsUseCase(mainBossId)
                }.await()
                val deferreds = listOf(
                    async {
                        val pointOfInterest = pointOfInterestUseCases.getPointsOfInterestBySubCategoryUseCase(
                            PointOfInterestSubCategory.FORSAKEN_ALTAR
                        )
                        _relatedForsakenAltar.value = pointOfInterest.find {
                            it.id in relatedObjects
                        }


                    },
                    async {
                        val biome = biomeUseCases.getLocalBiomesUseCase().first()
                        _relatedBiome.value = biome.find {
                            it.id in relatedObjects
                        }
                    },
                    async {
                       val materials =  materialUseCases.getMaterialsBySubCategory(
                           subCategory = MaterialSubCategory.FORSAKEN_ALTAR_OFFERING,
                       )
                        _relatedSummoningItems.value = materials.mapNotNull { material ->
                            val relation = relatedObjects.find { it. == material.id }
                            relation?.quantity?.let { quantity ->
                                "${material.name} x $quantity"
                            }
                        }


                    },
                    async {
                        _dropItems.value =  materialUseCases.getMaterialsByIds(relatedObjects)
                    }
                )

                deferreds.awaitAll()

            }


        } catch (e: Exception) {
            Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
        }
    }
}