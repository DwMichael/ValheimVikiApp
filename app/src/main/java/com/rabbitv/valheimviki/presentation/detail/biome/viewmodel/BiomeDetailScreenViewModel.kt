@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedListFlowGated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BiomeDetailScreenViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val biomeUseCases: BiomeUseCases,
	private val creaturesUseCase: CreatureUseCases,
	private val relationsUseCases: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val treeUseCases: TreeUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	var biomeId = mutableStateOf("")
		private set
	var initialImageUrl = mutableStateOf("")
		private set
	var initialTitle = mutableStateOf("")
		private set

	init {
		biomeId.value = savedStateHandle.toRoute<WorldDetailDestination.BiomeDetail>().biomeId
		initialImageUrl.value =
			savedStateHandle.toRoute<WorldDetailDestination.BiomeDetail>().imageUrl
		initialTitle.value = savedStateHandle.toRoute<WorldDetailDestination.BiomeDetail>().title
	}

	private val contentStart = MutableStateFlow(false)

	fun startContent() {
		contentStart.value = true
	}

	private val _biomeFlow: StateFlow<Biome?> =
		biomeUseCases.getBiomeByIdUseCase(biomeId.value)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


	private val relationsIds: StateFlow<List<String>> =
		relationsUseCases.getRelatedIdsUseCase(biomeId.value)
			.map { list -> list.map { it.id } }
			.distinctUntilChanged()
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())


	private val _mainBossState: Flow<UIState<MainBoss?>> =
		relationsIds
			.flatMapLatest { ids ->
				creaturesUseCase
					.getCreatureByRelationAndSubCategory(ids, CreatureSubCategory.BOSS)
			}
			.map { it?.toMainBoss() }
			.distinctUntilChanged()
			.map<MainBoss?, UIState<MainBoss?>> { UIState.Success(it) }
			.catch { e -> emit(UIState.Error(e.message ?: "Error fetching main boss")) }
			.flowOn(defaultDispatcher)


	private val _creaturesState: Flow<UIState<List<Creature>>> =
		relatedListFlowGated(
			idsFlow = relationsIds,
			contentStart = contentStart,
			fetcher = { ids -> creaturesUseCase.getCreaturesByIds(ids) },
			sortBy = { it.order }
		).flowOn(defaultDispatcher)


	private val _oreDepositsState: Flow<UIState<List<OreDeposit>>> =
		relatedListFlowGated(
			idsFlow = relationsIds,
			contentStart = contentStart,
			fetcher = { ids -> oreDepositUseCases.getOreDepositsByIdsUseCase(ids) },
			sortBy = { it.order }
		).flowOn(defaultDispatcher)

	private val _materialsState: Flow<UIState<List<Material>>> = relatedListFlowGated(
		idsFlow = relationsIds,
		contentStart = contentStart,
		fetcher = { ids -> materialUseCases.getMaterialsByIds(ids) },
		sortBy = { it.order }
	).flowOn(defaultDispatcher)

	private val _poiState: Flow<UIState<List<PointOfInterest>>> = relatedListFlowGated(
		idsFlow = relationsIds,
		contentStart = contentStart,
		fetcher = { ids -> pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids) },
		sortBy = { it.order }
	).flowOn(defaultDispatcher)

	private val _treesState: Flow<UIState<List<Tree>>> = relatedListFlowGated(
		idsFlow = relationsIds,
		contentStart = contentStart,
		fetcher = { ids -> treeUseCases.getTreesByIdsUseCase(ids) },
		sortBy = { it.order }
	).flowOn(defaultDispatcher)


	val biomeUiState: StateFlow<BiomeDetailUiState> =
		combine(
			_biomeFlow,
			_mainBossState,
			_creaturesState,
			_oreDepositsState,
			_materialsState,
			_poiState,
			_treesState,
			favoriteUseCases.isFavorite(biomeId.value)
		) { biome, boss, creatures, ores, materials, poi, trees, fav ->
			BiomeDetailUiState(
				biome = biome,
				mainBoss = boss,
				relatedCreatures = creatures,
				relatedOreDeposits = ores,
				relatedMaterials = materials,
				relatedPointOfInterest = poi,
				relatedTrees = trees,
				isFavorite = fav
			)
		}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BiomeDetailUiState())

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
