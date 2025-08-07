@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.biome.Biome
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
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

	private val _biomeFlow: Flow<Biome?> = biomeUseCases.getBiomeByIdUseCase(_biomeId)
	private val _relations: Flow<List<kotlin.String>> =
		relationsUseCase.getRelatedIdsUseCase(_biomeId)
			.map { relations -> relations.map { it.id } }
			.distinctUntilChanged()

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun <T> getRelatedDataFlow(fetcher: (List<kotlin.String>) -> Flow<T>): Flow<UIState<T>> {
		return _relations.flatMapLatest { ids ->
			if (ids.isEmpty()) {
				flowOf(UIState.Success(emptyList<Any>() as T))
			} else {
				fetcher(ids)
					.map<T, UIState<T>> { data -> UIState.Success(data) }
					.catch { e -> emit(UIState.Error(e.message ?: "Unknown error")) }
			}
		}.flowOn(defaultDispatcher)
	}

	private val _mainBossState: Flow<UIState<MainBoss?>> = getRelatedDataFlow { ids ->
		creaturesUseCase.getCreatureByRelationAndSubCategory(ids, CreatureSubCategory.BOSS)
			.map { it?.toMainBoss() }
	}

	private val _creaturesState: Flow<UIState<List<Creature>>> = getRelatedDataFlow { ids ->
		creaturesUseCase.getCreaturesByIds(ids)
	}

	private val _oreDepositsState: Flow<UIState<List<OreDeposit>>> = getRelatedDataFlow { ids ->
		oreDepositUseCases.getOreDepositsByIdsUseCase(ids)
	}

	private val _materialsState: Flow<UIState<List<Material>>> = getRelatedDataFlow { ids ->
		materialUseCases.getMaterialsByIds(ids)
	}

	private val _poiState: Flow<UIState<List<PointOfInterest>>> = getRelatedDataFlow { ids ->
		pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
	}

	private val _treesState: Flow<UIState<List<Tree>>> = getRelatedDataFlow { ids ->
		treeUseCases.getTreesByIdsUseCase(ids)
	}

	private val _isFavorite: Flow<Boolean> = favoriteUseCases.isFavorite(_biomeId)

	val biomeUiState: StateFlow<BiomeDetailUiState> = combine(
		_biomeFlow,
		_mainBossState,
		_creaturesState,
		_oreDepositsState,
		_materialsState,
		_poiState,
		_treesState,
		_isFavorite
	) { biome, mainBoss, creatures, oreDeposits, materials, poi, trees, isFavorite ->
		BiomeDetailUiState(
			biome = biome,
			mainBoss = mainBoss,
			relatedCreatures = creatures,
			relatedOreDeposits = oreDeposits,
			relatedMaterials = materials,
			relatedPointOfInterest = poi,
			relatedTrees = trees,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
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