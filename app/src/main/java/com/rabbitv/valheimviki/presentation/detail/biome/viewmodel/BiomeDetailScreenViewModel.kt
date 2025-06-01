@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.detail.biome_detail_state.UiBiomeDetailState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
    private val biomeId = savedStateHandle.get<String>(Constants.BIOME_ARGUMENT_KEY).toString()
    private val _biome: Flow<Biome?> =
        biomeUseCases.getBiomeByIdUseCase(biomeId).distinctUntilChanged()
    private val relatedItemsFlow: Flow<List<RelatedItem>> =
        relationsUseCase.getRelatedIdsUseCase(biomeId)
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), replay = 1)

    private val _mainBoss: Flow<MainBoss?> = relatedItemsFlow.flatMapLatest { relatedItems ->
        val relatedIds = relatedItems.map { it.id }
        creaturesUseCase.getCreatureByRelationAndSubCategory(
            relatedIds,
            CreatureSubCategory.BOSS
        ).map { creature ->
            creature?.let { nonNullCreature ->
                CreatureFactory.createFromCreature<MainBoss>(nonNullCreature)
            }
        }
    }.distinctUntilChanged()

    private val _relatedCreatures: Flow<List<Creature>> =
        relatedItemsFlow.flatMapLatest { relatedItems ->
            val relatedIds = relatedItems.map { it.id }
            creaturesUseCase.getCreaturesByIds(
                relatedIds
            )
        }.distinctUntilChanged()

    private val _relatedOreDeposits: Flow<List<OreDeposit>> =
        relatedItemsFlow.flatMapLatest { relatedItems ->
            val relatedIds = relatedItems.map { it.id }
            oreDepositUseCases.getOreDepositsByIdsUseCase(
                relatedIds
            )
        }.distinctUntilChanged()

    private val _relatedMaterials: Flow<List<Material>> =
        relatedItemsFlow.flatMapLatest { relatedItems ->
            val relatedIds = relatedItems.map { it.id }
            materialUseCases.getMaterialsByIds(
                relatedIds
            )
        }.distinctUntilChanged()
    private val _relatedPointOfInterest: Flow<List<PointOfInterest>> =
        relatedItemsFlow.flatMapLatest { relatedItems ->
            val relatedIds = relatedItems.map { it.id }
            pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(
                relatedIds
            )
        }.distinctUntilChanged()
    private val _relatedTrees: Flow<List<Tree>> =
        relatedItemsFlow.flatMapLatest { relatedItems ->
            val relatedIds = relatedItems.map { it.id }
            treeUseCases.getTreesByIdsUseCase(
                relatedIds
            )
        }.distinctUntilChanged()


    val biomeUiState: StateFlow<UiBiomeDetailState> = combine(
        _biome,
        _mainBoss,
        _relatedCreatures,
        _relatedOreDeposits,
        _relatedMaterials,
        _relatedPointOfInterest,
        _relatedTrees,
    ) { values ->
        UiBiomeDetailState.Success(
            biome = values[0] as Biome?,
            mainBoss = values[1] as MainBoss?,
            relatedCreatures = values[2] as List<Creature>,
            relatedOreDeposits = values[3] as List<OreDeposit>,
            relatedMaterials = values[4] as List<Material>,
            relatedPointOfInterest = values[5] as List<PointOfInterest>,
            relatedTrees = values[6] as List<Tree>,
        ) as UiBiomeDetailState
    }.onStart {
        emit(UiBiomeDetailState.Loading)
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Problem accessing local data."
            else -> "An unexpected error occurred."
        }
        emit(UiBiomeDetailState.Error(errorMessage))
        Log.e("BiomeDetailViewModel", "Error in combined BiomeDetailState: $e")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = UiBiomeDetailState.Loading
    )


}