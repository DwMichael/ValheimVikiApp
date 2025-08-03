@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
	private val oreDepositUseCases: OreDepositUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _biomeId: String = checkNotNull(savedStateHandle[BIOME_ARGUMENT_KEY])
	private val _relations: StateFlow<List<String>> =
		relationsUseCase
			.getRelatedIdsUseCase(_biomeId)
			.distinctUntilChanged()
			.map { it.map { rel -> rel.id } }
			.flowOn(defaultDispatcher)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(3000),
				initialValue = emptyList(),
			)

	private val _mainBossFlow: Flow<MainBoss?> =
		_relations
			.flatMapLatest { rIds ->
				creaturesUseCase
					.getCreatureByRelationAndSubCategory(
						rIds,
						CreatureSubCategory.BOSS
					)

			}.map { creature -> creature?.toMainBoss() }.flowOn(defaultDispatcher)
			.onCompletion { error -> println("Error -> ${error?.message}") }
			.catch { e -> Log.e("Boss fetch error BiomeDetailViewModel", e.message.toString()) }

	private val _creaturesFlow: Flow<List<Creature>> = _relations
		.flatMapLatest { rIds ->
			creaturesUseCase.getCreaturesByIds(rIds)
		}.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e -> Log.e("Creatures fetch error BiomeDetailViewModel", e.message.toString()) }


	private val _oreDepositFlow: Flow<List<OreDeposit>> =
		_relations
			.flatMapLatest { rIds ->
				oreDepositUseCases.getOreDepositsByIdsUseCase(rIds)
			}.onCompletion { error -> println("Error -> ${error?.message}") }
			.catch { e ->
				Log.e(
					"Ore deposits fetch error BiomeDetailViewModel",
					e.message.toString()
				)
			}

	private val _poiFlow: Flow<List<PointOfInterest>> = _relations
		.flatMapLatest { rIds ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(rIds)
		}.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e ->
			Log.e(
				"PointOfInterest fetch error BiomeDetailViewModel",
				e.message.toString()
			)
		}

	private val _treesFlow: Flow<List<Tree>> = _relations
		.flatMapLatest { rIds ->
			treeUseCases.getTreesByIdsUseCase(rIds)
		}.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e -> Log.e("Trees fetch error BiomeDetailViewModel", e.message.toString()) }


	private val _materialsFlow: Flow<List<Material>> = _relations
		.flatMapLatest { rIds ->
			materialUseCases.getMaterialsByIds(rIds)
		}.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e -> Log.e("Materials fetch error BiomeDetailViewModel", e.message.toString()) }
	private val _worldObjectsDataFlow: Flow<Triple<List<OreDeposit>, List<PointOfInterest>, List<Tree>>> =
		combine(_oreDepositFlow, _poiFlow, _treesFlow) { ores, poi, trees ->
			Triple(ores, poi, trees)
		}.flowOn(defaultDispatcher)

	private val _creaturesDataFlow: Flow<Pair<MainBoss?, List<Creature>>> =
		combine(_mainBossFlow, _creaturesFlow) { boss, creatures ->
			boss to creatures
		}.flowOn(defaultDispatcher)
	val biomeUiState: StateFlow<BiomeDetailUiState> = combine(
		biomeUseCases.getBiomeByIdUseCase(biomeId = _biomeId),
		_creaturesDataFlow,
		_worldObjectsDataFlow,
		_materialsFlow,
		favoriteUseCases.isFavorite(_biomeId),
	) { biome, (mainBoss, creatures), (ores, poi, trees), materials, favorite ->

		BiomeDetailUiState(
			biome = biome,
			mainBoss = UIState.Success(mainBoss),
			relatedCreatures = UIState.Success(creatures),
			relatedOreDeposits = UIState.Success(ores),
			relatedMaterials = UIState.Success(materials),
			relatedPointOfInterest = UIState.Success(poi),
			relatedTrees = UIState.Success(trees),
			isFavorite = favorite,
		)
	}.flowOn(defaultDispatcher)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = BiomeDetailUiState()
		)


	fun toggleFavorite(favorite: Favorite, currentIsFavorite: Boolean) {
		viewModelScope.launch {
			if (currentIsFavorite) {
				favoriteUseCases.deleteFavoriteUseCase(favorite)
			} else {
				favoriteUseCases.addFavoriteUseCase(favorite)
			}
		}
	}
}