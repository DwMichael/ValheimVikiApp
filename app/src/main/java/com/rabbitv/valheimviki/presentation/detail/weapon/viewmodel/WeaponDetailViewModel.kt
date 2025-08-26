package com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.navigation.EquipmentDetailDestination
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
class WeaponDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val weaponUseCases: WeaponUseCases,
	private val relationUseCase: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val foodUseCases: FoodUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _weaponId: String =
		savedStateHandle.toRoute<EquipmentDetailDestination.WeaponDetail>().weaponId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_weaponId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _weaponFlow: StateFlow<Weapon?> = weaponUseCases.getWeaponByIdUseCase(_weaponId)
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationsObjects =
		relationUseCase.getRelatedIdsUseCase(_weaponId).stateIn(
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

	private val _relatedMaterials =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			materialUseCases.getMaterialsByIds(ids)
				.map { list ->
					list.map { material ->
						val relatedItem = relatedItems[material.id]
						MaterialUpgrade(
							material = material,
							quantityList = listOf(
								relatedItem?.quantity,
								relatedItem?.quantity2star,
								relatedItem?.quantity3star,
								relatedItem?.quantity4star
							)
						)
					}
				}
		}.distinctUntilChanged()
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	private val _relatedFoodAsMaterials =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			foodUseCases.getFoodListByIdsUseCase(ids)
				.map { list ->
					list.map { food ->
						val relatedItem = relatedItems[food.id]
						FoodAsMaterialUpgrade(
							materialFood = food,
							quantityList = listOf(
								relatedItem?.quantity,
								relatedItem?.quantity2star,
								relatedItem?.quantity3star,
								relatedItem?.quantity4star
							)
						)
					}
				}
		}.distinctUntilChanged()
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)


	private val _relatedCraftingObjects = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> craftingObjectUseCases.getCraftingObjectByIds(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<WeaponUiState> = combine(
		_weaponFlow,
		_relatedMaterials,
		_relatedFoodAsMaterials,
		_relatedCraftingObjects,
		_isFavorite
	) { weapon, materials, foodAsMaterials, craftingObjects, isFavorite ->
		WeaponUiState(
			weapon = weapon,
			materials = materials,
			foodAsMaterials = foodAsMaterials,
			craftingObjects = craftingObjects,
			isFavorite = isFavorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = WeaponUiState()
	)

	fun uiEvent(event: WeaponDetailUiEvent) {
		when (event) {
			WeaponDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_weaponFlow.value?.let { weapon ->
						favoriteUseCases.toggleFavoriteUseCase(
							weapon.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}
