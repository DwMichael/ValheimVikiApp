package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
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
import kotlinx.coroutines.flow.StateFlow
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
    private val miniBossId: String = checkNotNull(savedStateHandle[Constants.MINI_BOSS_ARGUMENT_KEY])

    private val _miniBoss = MutableStateFlow<MiniBoss?>(null)
    val miniBoss: StateFlow<MiniBoss?> = _miniBoss

    private val _primarySpawn = MutableStateFlow<PointOfInterest?>(null)
    val primarySpawn: StateFlow<PointOfInterest?> = _primarySpawn

    private val _npc = MutableStateFlow<NPC?>(null)
    val npc: StateFlow<NPC?> = _npc

    private val _dropItems = MutableStateFlow<List<Material?>>(emptyList())
    val dropItems: StateFlow<List<Material?>> = _dropItems

    private val _trophy = MutableStateFlow<Material?>(null)
    val trophy: StateFlow<Material?> = _trophy


    init {
        launch()
    }


    fun launch() {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                creatureUseCases.getCreatureById(miniBossId).let {
                    _miniBoss.value = CreatureFactory.createFromCreature(it)
                }
                val relatedObjects: List<RelatedItem> = async {
                    relationUseCases.getRelatedIdsUseCase(miniBossId)
                }.await()

                val relatedIds = relatedObjects.map { it.id }

                val deferreds = listOf(
//                    async {
//                        pointOfInterestUseCases.ge
//                    },
                    async {

                        val materials = materialUseCases.getMaterialsBySubCategory(
                            MaterialSubCategory.MINI_BOSS_DROP
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