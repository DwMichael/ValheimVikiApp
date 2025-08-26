package com.rabbitv.valheimviki.presentation.detail.material.general.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.navigation.MaterialDetailDestination
import com.rabbitv.valheimviki.presentation.detail.material.general.model.GeneralMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.general.model.GeneralMaterialUiState
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GeneralMaterialDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val treeUseCases: TreeUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _materialId: String =
		savedStateHandle.toRoute<MaterialDetailDestination.GeneralMaterialDetail>().generalMaterialId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_materialId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _materialFlow: StateFlow<Material?> =
		materialUseCases.getMaterialById(_materialId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationsObjects: StateFlow<List<RelatedItem>> =
		relationUseCases.getRelatedIdsUseCase(_materialId)
			.distinctUntilChanged()
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

	private val _relatedBiomes = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> biomeUseCases.getBiomesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedOreDeposits = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> oreDepositUseCases.getOreDepositsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedPointOfInterests = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedTrees = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> treeUseCases.getTreesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _requiredCraftingStation = relatedDataFlow(
		idsFlow = _relationsObjects
			.map { list ->
				list.filter { it.relationType == RelationType.PRODUCES.toString() }
					.map { item -> item.id }
			},
		fetcher = { ids -> craftingUseCases.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<GeneralMaterialUiState> = combine(
		_materialFlow,
		_relatedBiomes,
		_requiredCraftingStation,
		_relatedPointOfInterests,
		_relatedOreDeposits,
		_relatedTrees,
		_isFavorite
	) { material, biomes, requiredCraftingStation, pointOfInterests, oreDeposits, trees, isFavorite ->
		GeneralMaterialUiState(
			material = material,
			biomes = biomes,
			craftingStations = requiredCraftingStation,
			pointOfInterests = pointOfInterests,
			oreDeposits = oreDeposits,
			trees = trees,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = GeneralMaterialUiState()
	)

	fun uiEvent(event: GeneralMaterialUiEvent) {
		when (event) {
			GeneralMaterialUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_materialFlow.value?.let { material ->
						favoriteUseCases.toggleFavoriteUseCase(
							material.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}