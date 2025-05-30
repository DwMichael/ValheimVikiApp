package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailUiState
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiomeDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val biomeUseCases: BiomeUseCases,
    private val creaturesUseCase: CreatureUseCases,
    private val relationsUseCase: RelationUseCases,
    private val materialUseCases: MaterialUseCases,
    private val pointOfInterestUseCases: PointOfInterestUseCases,
    private val treeUseCases: TreeUseCases,
    private val oreDepositUseCases: OreDepositUseCases
) : ViewModel() {
    private val _biome = MutableStateFlow<Biome?>(null)
    private val _mainBoss = MutableStateFlow<MainBoss?>(null)
    private val _relatedCreatures = MutableStateFlow<List<Creature>>(emptyList())
    private val _relatedOreDeposits = MutableStateFlow<List<OreDeposit>>(emptyList())
    private val _relatedMaterials = MutableStateFlow<List<Material>>(emptyList())
    private val _relatedPointOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
    private val _relatedTrees = MutableStateFlow<List<Tree>>(emptyList())


    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _biome,
        _mainBoss,
        _relatedCreatures,
        _relatedOreDeposits,
        _relatedMaterials,
        _relatedPointOfInterest,
        _relatedTrees,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        (BiomeDetailUiState(
            biome = values[0] as Biome?,
            mainBoss = values[1] as MainBoss?,
            relatedCreatures = values[2] as List<Creature>,
            relatedOreDeposits = values[3] as List<OreDeposit>,
            relatedMaterials = values[4] as List<Material>,
            relatedPointOfInterest = values[5] as List<PointOfInterest>,
            relatedTrees = values[6] as List<Tree>,
            isLoading = values[7] as Boolean,
            error = values[8] as String?
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = BiomeDetailUiState()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val biomeId = savedStateHandle.get<String>(Constants.BIOME_ARGUMENT_KEY).toString()

                val biomeData = biomeUseCases.getBiomeByIdUseCase(biomeId = biomeId).firstOrNull()
                _biome.value = biomeData

                val relatedObjects: List<RelatedItem> = async {
                    relationsUseCase.getRelatedIdsUseCase(biomeId)
                }.await()

                val relatedIds = relatedObjects.map { it.id }
                try {
                    relatedObjects.let { ids ->
                        creaturesUseCase.getCreatureByRelationAndSubCategory(
                            relatedIds,
                            CreatureSubCategory.BOSS
                        )?.toMainBoss().let { boss ->
                            _mainBoss.value = boss
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Boss fetch error BiomeDetailViewModel", e.message.toString())
                    _mainBoss.value = null
                }

                try {
                    val creatures = creaturesUseCase.getCreaturesByIds(relatedIds)
                    _relatedCreatures.value = creatures
                } catch (e: Exception) {
                    Log.e("Creatures fetch error BiomeDetailViewModel", e.message.toString())
                }

                try {
                    val oreDeposits = oreDepositUseCases.getOreDepositsByIdsUseCase(relatedIds)
                    _relatedOreDeposits.value = oreDeposits
                } catch (e: Exception) {
                    Log.e("Ore deposits fetch error BiomeDetailViewModel", e.message.toString())
                }
                try {
                    val materials = materialUseCases.getMaterialsByIds(relatedIds)
                    _relatedMaterials.value = materials
                } catch (e: Exception) {
                    Log.e("Materials fetch error BiomeDetailViewModel", e.message.toString())
                }

                try {
                    val pointOfInterest =
                        pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(relatedIds)
                    _relatedPointOfInterest.value = pointOfInterest
                } catch (e: Exception) {
                    Log.e("PointOfInterest fetch error BiomeDetailViewModel", e.message.toString())
                }

                try {
                    val trees = treeUseCases.getTreesByIdsUseCase(relatedIds)
                    _relatedTrees.value = trees
                } catch (e: Exception) {
                    Log.e("Trees fetch error BiomeDetailViewModel", e.message.toString())
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
                _isLoading.value = false
            }
        }
    }
}