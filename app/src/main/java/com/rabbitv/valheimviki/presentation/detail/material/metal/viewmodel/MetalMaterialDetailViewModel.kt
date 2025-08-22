package com.rabbitv.valheimviki.presentation.detail.material.metal.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.metal.model.MetalMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.metal.model.MetalMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft
import com.rabbitv.valheimviki.utils.Constants.METAL_MATERIAL_KEY
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MetalMaterialDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _materialId: String = checkNotNull(savedStateHandle[METAL_MATERIAL_KEY])
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

	private val _relatedCreatures = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> creatureUseCases.getCreaturesByIds(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedOreDeposits = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> oreDepositUseCases.getOreDepositsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedPointOfInterests = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _requiredCraftingStation = relatedDataFlow(
		idsFlow = _relationsObjects
			.map { list -> 
				list.filter { it.relationType == RelationType.PRODUCES.toString() }
					.map { item -> item.id }
			},
		fetcher = { ids -> craftingUseCases.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)

	private val _requiredMaterials: StateFlow<UIState<List<MaterialToCraft>>> =
		_relationsObjects
			.filter { it.isNotEmpty() }
			.flatMapLatest { relatedObjects ->
				val relationToBuildIds = relatedObjects
					.filter { it.relationType == RelationType.TO_BUILD.toString() }
					.map { it.id }
				
				if (relationToBuildIds.isEmpty()) {
					kotlinx.coroutines.flow.flowOf(emptyList<MaterialToCraft>())
				} else {
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					materialUseCases.getMaterialsByIds(relationToBuildIds)
						.map { materials ->
							materials.map { material ->
								val relatedItem = relatedItemsMap[material.id]
								val quantityList = listOf<Int?>(
									relatedItem?.quantity,
									relatedItem?.quantity2star,
									relatedItem?.quantity3star
								)
								MaterialToCraft(
									material = material,
									quantityList = quantityList
								)
							}
						}
				}
			}
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)
			.stateIn(
				viewModelScope,
				SharingStarted.WhileSubscribed(5_000),
				UIState.Loading
			)

	val uiState: StateFlow<MetalMaterialUiState> = combine(
		_materialFlow,
		_relatedBiomes,
		_relatedCreatures,
		_requiredCraftingStation,
		_relatedPointOfInterests,
		_relatedOreDeposits,
		_requiredMaterials,
		_isFavorite
	) { material, biomes, creatures, requiredCraftingStation, pointOfInterests, oreDeposits, requiredMaterials, isFavorite ->
		MetalMaterialUiState(
			material = material,
			biomes = biomes,
			creatures = creatures,
			craftingStations = requiredCraftingStation,
			pointOfInterests = pointOfInterests,
			oreDeposits = oreDeposits,
			requiredMaterials = requiredMaterials,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = MetalMaterialUiState()
	)

	fun uiEvent(event: MetalMaterialUiEvent) {
		when (event) {
			MetalMaterialUiEvent.ToggleFavorite -> {
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