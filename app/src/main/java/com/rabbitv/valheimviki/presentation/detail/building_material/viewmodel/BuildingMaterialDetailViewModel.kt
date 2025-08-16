package com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.building_material.model.RequiredFood
import com.rabbitv.valheimviki.presentation.detail.building_material.model.RequiredMaterial
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNCHECKED_CAST")
@HiltViewModel
class BuildingMaterialDetailViewModel @Inject constructor(
	private val buildingMaterialUseCases: BuildMaterialUseCases,
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val foodUseCases: FoodUseCases,
	private val relationsUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	
	private val _buildingMaterialId: String =
		savedStateHandle.toRoute<BuildingDetailDestination.BuildingMaterialDetail>().buildingMaterialId
	private val _buildingMaterial =
		buildingMaterialUseCases.getBuildMaterialById(_buildingMaterialId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _isFavorite = favoriteUseCases.isFavorite(_buildingMaterialId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	private val _relationObjects =
		relationsUseCases.getRelatedIdsUseCase(_buildingMaterialId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)

	private val relatedItemsMap = _relationObjects.filter { it.isNotEmpty() }.map { list ->
		list.associateBy { it.id }
	}.flowOn(defaultDispatcher)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyMap()
		)
	private val _relatedIds = _relationObjects.filter { it.isNotEmpty() }.map { list ->
		list.map { it.id }
	}.flowOn(defaultDispatcher)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val _materials = _relatedIds.flatMapLatest { ids ->
		combine(
			materialUseCases.getMaterialsByIds(ids),
			relatedItemsMap
		) { materials, currentItemsMap ->
			materials.mapNotNull { material ->
				val relatedItem = currentItemsMap[material.id]
				relatedItem?.let {
					RequiredMaterial(
						itemDrop = material,
						quantityList = listOf(
							it.quantity,
							it.quantity2star,
							it.quantity3star
						),
						chanceStarList = listOf(
							it.chance1star,
							it.chance2star,
							it.chance3star
						),
						droppableType = DroppableType.MATERIAL,
					)
				}
			}
		}
	}.map { UIState.Success(it) }
		.flowOn(defaultDispatcher)

	private val _foods = _relatedIds.flatMapLatest { ids ->
		combine(
			foodUseCases.getFoodListByIdsUseCase(ids),
			relatedItemsMap
		) { foods, currentItemsMap ->
			foods.mapNotNull { food ->
				val relatedItem = currentItemsMap[food.id]
				relatedItem?.let {
					RequiredFood(
						itemDrop = food,
						quantityList = listOf(
							it.quantity,
							it.quantity2star,
							it.quantity3star
						),
						chanceStarList = listOf(
							it.chance1star,
							it.chance2star,
							it.chance3star
						),
						droppableType = DroppableType.FOOD,
					)
				}
			}
		}
	}.map { UIState.Success(it) }
		.flowOn(defaultDispatcher)

	private val _requiredCraftingStation = relatedDataFlow(
		idsFlow = _relatedIds,
		fetcher = { ids -> craftingUseCases.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)


	val uiState = combine(
		_buildingMaterial,
		_materials,
		_foods,
		_requiredCraftingStation,
		_isFavorite,
	) { buildingMaterial, materials, food, craftingStation, favorite ->
		BuildingMaterialUiState(
			buildingMaterial = buildingMaterial,
			materials = materials,
			foods = food,
			craftingStation = craftingStation,
			isFavorite = favorite,
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		BuildingMaterialUiState()
	)


	fun uiEvent(event: BuildingMaterialUiEvent) {
		when (event) {
			is BuildingMaterialUiEvent.ToggleFavorite ->
				viewModelScope.launch {
					_buildingMaterial.value?.let { bM ->
						favoriteUseCases.toggleFavoriteUseCase(
							bM.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
		}
	}
}