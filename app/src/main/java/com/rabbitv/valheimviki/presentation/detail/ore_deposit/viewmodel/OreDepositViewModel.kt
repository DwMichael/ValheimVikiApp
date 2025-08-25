package com.rabbitv.valheimviki.presentation.detail.ore_deposit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiEvent
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiState
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
class OreDepositViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val oreDepositUseCases: OreDepositUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val toolUseCases: ToolUseCases,
	private val craftingStation: CraftingObjectUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	
	private val _oreDepositId: String = savedStateHandle.toRoute<WorldDetailDestination.OreDepositDetail>().oreDepositId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_oreDepositId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _oreDepositFlow: StateFlow<OreDeposit?> =
		oreDepositUseCases.getOreDepositByIdUseCase(_oreDepositId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_oreDepositId).stateIn(
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
	private val _relatedBiomes = relatedDataFlow(
		idsFlow = idsAndMap.mapLatest { (ids, list) -> ids },
		fetcher = { ids -> biomeUseCases.getBiomesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)


	private val _relatedMaterials =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			materialUseCases.getMaterialsByIds(ids)
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

	private val _relatedTools = relatedDataFlow(
		idsFlow =  idsAndMap.mapLatest { (ids, list) -> ids },
		fetcher = { ids -> toolUseCases.getToolsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedCraftingStation = relatedDataFlow(
		idsFlow =  idsAndMap.mapLatest { (ids, list) -> ids },
		fetcher = { ids -> craftingStation.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<OreDepositUiState> = combine(
		_oreDepositFlow,
		_relatedBiomes,
		_relatedMaterials,
		_relatedTools,
		_relatedCraftingStation,
		_isFavorite
	) { oreDeposit, biomes, materials, tools, craftingStation, isFavorite ->
		OreDepositUiState(
			oreDeposit = oreDeposit,
			relatedBiomes = biomes,
			relatedMaterials = materials,
			relatedTools = tools,
			craftingStation = craftingStation,
			isFavorite = isFavorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = OreDepositUiState()
	)

	fun uiEvent(event: OreDepositUiEvent) {
		when (event) {
			OreDepositUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_oreDepositFlow.value?.let { oreDeposit ->
						favoriteUseCases.toggleFavoriteUseCase(
							oreDeposit.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}