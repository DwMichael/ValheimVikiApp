package com.rabbitv.valheimviki.presentation.detail.tool.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.presentation.detail.tool.model.ToolDetailUiState
import com.rabbitv.valheimviki.utils.Constants.TOOL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class ToolDetailViewModel @Inject constructor(
	private val toolUseCases: ToolUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	private val savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _toolId: String = checkNotNull(savedStateHandle[TOOL_KEY])
	private val _tool = MutableStateFlow<ItemTool?>(null)
	private val _relatedCraftingObject = MutableStateFlow<CraftingObject?>(null)
	private val _relatedOreDeposits = MutableStateFlow<List<OreDeposit>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<MaterialUpgrade>>(emptyList())
	private val _relatedNpc = MutableStateFlow<Creature?>(null)
	private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
	private val _error: MutableStateFlow<String?> = MutableStateFlow(null)
	private val _isFavorite = favoriteUseCases.isFavorite(_toolId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	val uiState: StateFlow<ToolDetailUiState> = combine(
		_tool,
		_relatedCraftingObject,
		_relatedMaterials,
		_relatedOreDeposits,
		_relatedNpc,
		_isFavorite,
		_isLoading,
		_error
	) { values ->
		ToolDetailUiState(
			tool = values[0] as ItemTool,
			relatedCraftingStation = values[1] as CraftingObject?,
			relatedMaterials = values[2] as List<MaterialUpgrade>,
			relatedOreDeposits = values[3] as List<OreDeposit>,
			relatedNpc = values[4] as Creature?,
			isFavorite = values[5] as Boolean,
			isLoading = values[6] as Boolean,
			error = values[7] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ToolDetailUiState()
	)


	init {
		loadFoodData()
	}


	internal fun loadFoodData() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true


				launch { _tool.value = toolUseCases.getToolByIdUseCase(_toolId).first() }

				val relatedObjects: List<RelatedItem> =
					relationUseCases.getRelatedIdsForUseCase(_toolId).first()

				val relatedIds = relatedObjects.map { it.id }


				val craftingDeferred = async {
					_relatedCraftingObject.value =
						craftingObjectUseCases.getCraftingObjectByIds(relatedIds).first()

				}

				val materialsDeferred = async {
					val materials = materialUseCases.getMaterialsByIds(relatedIds).first()

					val tempList = mutableListOf<MaterialUpgrade>()
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					materials.forEach { material ->
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf<Int?>(
							relatedItem?.quantity,
							relatedItem?.quantity2star,
							relatedItem?.quantity3star
						)
						tempList.add(
							MaterialUpgrade(
								material = material,
								quantityList = quantityList,
							)
						)
					}
					_relatedMaterials.value = tempList
				}

				val oreDepositDeferred = async {
					_relatedOreDeposits.value =
						oreDepositUseCases.getOreDepositsByIdsUseCase(relatedIds).first()
							.sortedBy { it.order }
				}

				val npcDeferred =
					async {//FOr now there is only one npc that sell specific item but if there will be more change it
						val npc =
							creatureUseCases.getCreaturesByIds(relatedIds).first()
						if (npc.isNotEmpty()) {
							_relatedNpc.value = npc[0]
						}
					}

				awaitAll(
					craftingDeferred,
					materialsDeferred,
					oreDepositDeferred,
					npcDeferred
				)

			} catch (e: Exception) {
				Log.e("General fetch error toolDetailViewModel", e.message.toString())
				_isLoading.value = false
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun toggleFavorite(favorite: Favorite, currentIsFavorite: Boolean) {
		viewModelScope.launch {
			if (currentIsFavorite) {
				favoriteUseCases.deleteFavoriteUseCase(favorite)
			} else {
				favoriteUseCases.addFavoriteUseCase(favorite)
			}
		}
	}
}