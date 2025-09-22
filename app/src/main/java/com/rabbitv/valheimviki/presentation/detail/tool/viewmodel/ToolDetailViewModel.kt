package com.rabbitv.valheimviki.presentation.detail.tool.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.navigation.EquipmentDetailDestination
import com.rabbitv.valheimviki.presentation.detail.tool.model.ToolDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.tool.model.ToolDetailUiState
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ToolDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val toolUseCases: ToolUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _toolId: String =
		savedStateHandle.toRoute<EquipmentDetailDestination.ToolDetail>().toolId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_toolId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _toolFlow: StateFlow<ItemTool?> =
		toolUseCases.getToolByIdUseCase(_toolId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationsObjects =
		relationUseCases.getRelatedIdsUseCase(_toolId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<RelatedData> =
		_relationsObjects
			.map { list ->
				val relatedMap = list.associateBy(RelatedItem::id)
				val ids = relatedMap.keys.sorted()
				RelatedData(ids = ids, relatedMap = relatedMap)
			}
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)
	private val _relatedCraftingObject = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> craftingObjectUseCases.getCraftingObjectByIds(ids) }
	).flowOn(defaultDispatcher)


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
			.onStart { emit(UIState.Success(emptyList())) }
			.stateIn(
				viewModelScope,
				SharingStarted.WhileSubscribed(5_000),
				UIState.Success(emptyList())
			)

	private val _relatedOreDeposits = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> oreDepositUseCases.getOreDepositsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedNpc = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> creatureUseCases.getCreaturesByIds(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<ToolDetailUiState> = combine(
		_toolFlow,
		_relatedCraftingObject,
		_relatedMaterials,
		_relatedOreDeposits,
		_relatedNpc,
		_isFavorite
	) { tool, relatedCraftingObject, relatedMaterials, relatedOreDeposits, relatedNpc, isFavorite ->
		ToolDetailUiState(
			tool = tool,
			relatedCraftingStation = relatedCraftingObject,
			relatedMaterials = relatedMaterials,
			relatedOreDeposits = relatedOreDeposits,
			relatedNpc = relatedNpc,
			isFavorite = isFavorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ToolDetailUiState()
	)

	fun uiEvent(event: ToolDetailUiEvent) {
		when (event) {
			ToolDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_toolFlow.value?.let { tool ->
						favoriteUseCases.toggleFavoriteUseCase(
							tool.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}