package com.rabbitv.valheimviki.presentation.detail.material.offerings.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.MaterialDetailDestination
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiState
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
class OfferingsDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _materialId: String =
		savedStateHandle.toRoute<MaterialDetailDestination.OfferingsDetail>().offeringsMaterialId
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

	private val _relatedPassiveCreatures = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			creatureUseCases.getCreaturesByRelationAndSubCategory(
				ids,
				CreatureSubCategory.PASSIVE_CREATURE
			)
				.map { creatures -> creatures.toPassiveCreatures() }
		}
	).flowOn(defaultDispatcher)

	private val _relatedAggressiveCreatures = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			creatureUseCases.getCreaturesByRelationAndSubCategory(
				ids,
				CreatureSubCategory.AGGRESSIVE_CREATURE
			)
				.map { creatures -> creatures.toAggressiveCreatures() }
		}
	).flowOn(defaultDispatcher)

	private val _relatedPointsOfInterest = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
				.map { points ->
					points.filter { it.subCategory != PointOfInterestSubCategory.FORSAKEN_ALTAR.toString() }
				}
		}
	).flowOn(defaultDispatcher)

	private val _relatedAltars = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
				.map { points ->
					points.filter { it.subCategory == PointOfInterestSubCategory.FORSAKEN_ALTAR.toString() }
				}
		}
	).flowOn(defaultDispatcher)

	private val _relatedCraftingStation = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> craftingObjectUseCases.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<OfferingUiState> = combine(
		_materialFlow,
		_relatedPassiveCreatures,
		_relatedAggressiveCreatures,
		_relatedPointsOfInterest,
		_relatedAltars,
		_relatedCraftingStation,
		_isFavorite
	) { material, passiveCreatures, aggressiveCreatures, pointsOfInterest, altars, craftingStation, isFavorite ->
		OfferingUiState(
			material = material,
			passive = passiveCreatures,
			aggressive = aggressiveCreatures,
			pointsOfInterest = pointsOfInterest,
			altars = altars,
			craftingStation = craftingStation,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = OfferingUiState()
	)

	fun uiEvent(event: OfferingUiEvent) {
		when (event) {
			OfferingUiEvent.ToggleFavorite -> {
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