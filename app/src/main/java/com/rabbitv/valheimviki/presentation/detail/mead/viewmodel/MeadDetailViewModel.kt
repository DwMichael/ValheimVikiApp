package com.rabbitv.valheimviki.presentation.detail.mead.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.ConsumableDetailDestination
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiState
import com.rabbitv.valheimviki.presentation.detail.mead.model.RecipeMeadData
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
import kotlin.collections.map

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MeadDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val meadUseCases: MeadUseCases,
	private val foodUseCases: FoodUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	
	private val _meadId: String = savedStateHandle.toRoute<ConsumableDetailDestination.MeadDetail>().meadId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_meadId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _meadFlow: StateFlow<Mead?> =
		meadUseCases.getMeadByIdUseCase(_meadId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_meadId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<RelatedData> =
		_relationObjects
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
	private val _relatedCraftingObject = relatedDataFlow(
		idsFlow = idsAndMap.mapLatest { (ids, list) -> ids },
		fetcher = { ids -> craftingObjectUseCases.getCraftingObjectByIds(ids) }
	).flowOn(defaultDispatcher)


	private val _combineRecipeMaterials =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			combine(
				foodUseCases.getFoodListByIdsUseCase(ids),
				meadUseCases.getMeadsByIdsUseCase(ids),
				materialUseCases.getMaterialsByIds(ids)
			) { foods, meads, materials ->
				val foodsData = foods.map { item ->
					val rel = relatedItems[item.id]
					RecipeMaterialData(
						itemDrop = item,
						quantityList = listOf(rel?.quantity),
						chanceStarList = emptyList()
					)
				}

				val meadsData = meads.map { item ->
					val rel = relatedItems[item.id]
					RecipeMaterialData(
						itemDrop = item,
						quantityList = listOf(rel?.quantity),
						chanceStarList = emptyList()
					)
				}

				val materialsData = materials.map { item ->
					val rel = relatedItems[item.id]
					RecipeMaterialData(
						itemDrop = item,
						quantityList = listOf(rel?.quantity),
						chanceStarList = emptyList()
					)
				}
				val byId = (foodsData + meadsData + materialsData).associateBy { it.itemDrop.id }
				ids.mapNotNull { id -> byId[id] }
			}
		}
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	val uiState: StateFlow<MeadDetailUiState> = combine(
		_meadFlow,
		_relatedCraftingObject,
		_combineRecipeMaterials,
		_isFavorite
	) { mead, craftingObject, recipeItems, isFavorite ->
		MeadDetailUiState(
			mead = mead,
			craftingCookingStation = craftingObject,
			recipeItems = UIState.Success(recipeItems),
			isFavorite = isFavorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = MeadDetailUiState()
	)

	fun uiEvent(event: MeadDetailUiEvent) {
		when (event) {
			MeadDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_meadFlow.value?.let { mead ->
						favoriteUseCases.toggleFavoriteUseCase(
							mead.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}