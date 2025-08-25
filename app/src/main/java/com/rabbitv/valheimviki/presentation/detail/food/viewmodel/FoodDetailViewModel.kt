package com.rabbitv.valheimviki.presentation.detail.food.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.ConsumableDetailDestination
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNCHECKED_CAST")
@HiltViewModel
class FoodDetailViewModel @Inject constructor(
	private val foodUseCases: FoodUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	private val savedStateHandle: SavedStateHandle,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _foodId: String = savedStateHandle.toRoute<ConsumableDetailDestination.FoodDetail>().foodId
	private val _food = foodUseCases.getFoodByIdUseCase(_foodId)
		.stateIn(
			viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = null
		)

	private val _isFavorite = favoriteUseCases.isFavorite(_foodId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	private val _relationObjects =
		relationUseCases.getRelatedIdsForUseCase(_foodId)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList()
			)
	private val idsAndMap: Flow<RelatedData> =
		_relationObjects
			.map { list ->
				val relatedMap = list.associateBy(RelatedItem::id)
				val ids = relatedMap.keys.sorted()
				RelatedData(ids = ids, relatedMap = relatedMap)
			}
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)
	private val _foodForRecipe: Flow<UIState<List<RecipeFoodData>>> =
		idsAndMap.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(emptyList())
			} else {
				foodUseCases.getFoodListByIdsUseCase(ids).map { list ->
					list.map { food ->
						val r = currentItemsMap[food.id]
						RecipeFoodData(
							itemDrop = food,
							quantityList = listOf(r?.quantity)
						)
					}.sortedBy { it.itemDrop.id }
				}
			}
		}.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	private val _materialsForRecipe: Flow<UIState<List<RecipeMaterialData<Material>>>> =
		idsAndMap.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(emptyList())
			} else {
				materialUseCases.getMaterialsByIds(ids).map { list ->
					list.map { material ->
						val r = currentItemsMap[material.id]
						RecipeMaterialData(
							itemDrop = material,
							quantityList = listOf(r?.quantity),
							chanceStarList = emptyList(),
						)
					}.sortedBy { it.itemDrop.id }
				}
			}
		}.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	private val _craftingCookingStation =
		idsAndMap.flatMapLatest { (ids, _) ->
			if (ids.isEmpty()) {
				flowOf<CraftingObject?>(null)
			} else {
				craftingObjectUseCases.getCraftingObjectByIds(ids)
			}
		}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)


	val uiState: StateFlow<FoodDetailUiState> = combine(
		_food,
		_craftingCookingStation,
		_foodForRecipe,
		_materialsForRecipe,
		_isFavorite
	) { food, craftingCookingStation, foodForRecipe, materialsForRecipe, favorite ->
		FoodDetailUiState(
			food = food,
			craftingCookingStation = craftingCookingStation,
			foodForRecipe = foodForRecipe,
			materialsForRecipe = materialsForRecipe,
			isFavorite = favorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = FoodDetailUiState()
	)


	fun uiEvent(event: FoodDetailUiEvent) {
		when (event) {
			FoodDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_food.value?.let { bM ->
						favoriteUseCases.toggleFavoriteUseCase(
							bM.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}