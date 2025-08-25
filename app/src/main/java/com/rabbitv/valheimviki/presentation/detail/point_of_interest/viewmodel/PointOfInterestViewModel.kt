package com.rabbitv.valheimviki.presentation.detail.point_of_interest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiEvent
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiState
import com.rabbitv.valheimviki.utils.Constants.POINT_OF_INTEREST_KEY
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PointOfInterestViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val _pointOfInterestUseCases: PointOfInterestUseCases,
	private val _biomeUseCases: BiomeUseCases,
	private val _creatureUseCases: CreatureUseCases,
	private val _materialUseCases: MaterialUseCases,
	private val _relationUseCases: RelationUseCases,
	private val _weaponUseCases: WeaponUseCases,
	private val _foodUseCases: FoodUseCases,
	private val _oreDepositUseCase: OreDepositUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _pointOfInterestId: String = checkNotNull(savedStateHandle[POINT_OF_INTEREST_KEY])
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_pointOfInterestId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _pointOfInterestFlow: StateFlow<PointOfInterest?> =
		_pointOfInterestUseCases.getPointOfInterestByIdUseCase(_pointOfInterestId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
	private val _relationsObjects =
		_relationUseCases.getRelatedIdsUseCase(_pointOfInterestId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<RelatedData> =
		_relationsObjects
			.map { list ->
				val relatedMap = list.associateBy(RelatedItem::id)
				val ids = relatedMap.keys.sorted()
				RelatedData(
					ids = ids,
					relatedMap = relatedMap
				)
			}
			.filter { it.ids.isNotEmpty() }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)
	private val _relatedBiomes = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _biomeUseCases.getBiomesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedCreatures = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _creatureUseCases.getCreaturesByIds(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedWeapons = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _weaponUseCases.getWeaponsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedFoods = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _foodUseCases.getFoodListByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedOreDeposits = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _oreDepositUseCase.getOreDepositsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedOfferings = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> _materialUseCases.getMaterialsByIds(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedMaterialDrops =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			_materialUseCases.getMaterialsByIds(ids)
				.map { list ->
					list.map { material  ->
						val relatedItem = relatedItems[material.id]
						MaterialDrop(
							itemDrop = material,
							quantityList = listOf(relatedItem?.quantity),
							chanceStarList = listOf(relatedItem?.chance1star)
						)
					}
				}
		}.distinctUntilChanged()
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	val uiState: StateFlow<PointOfInterestUiState> = combine(
		_pointOfInterestFlow,
		_relatedBiomes,
		_relatedCreatures,
		_relatedWeapons,
		_relatedFoods,
		_relatedOreDeposits,
		_relatedOfferings,
		_relatedMaterialDrops,
		_isFavorite
	) { pointOfInterest, relatedBiomes, relatedCreatures, relatedWeapons, relatedFoods, relatedOreDeposits,
	    relatedOfferings, relatedMaterialDrops, isFavorite ->
		PointOfInterestUiState(
			pointOfInterest = pointOfInterest,
			relatedBiomes = relatedBiomes,
			relatedCreatures = relatedCreatures,
			relatedWeapons = relatedWeapons,
			relatedFoods = relatedFoods,
			relatedOreDeposits = relatedOreDeposits,
			relatedOfferings = relatedOfferings,
			relatedMaterialDrops = relatedMaterialDrops,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = PointOfInterestUiState()
	)

	fun uiEvent(event: PointOfInterestUiEvent) {
		when (event) {
			PointOfInterestUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_pointOfInterestFlow.value?.let { pointOfInterest ->
						favoriteUseCases.toggleFavoriteUseCase(
							pointOfInterest.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}