package com.rabbitv.valheimviki.presentation.detail.ore_deposit.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiState
import com.rabbitv.valheimviki.utils.Constants.ORE_DEPOSIT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class OreDepositViewModel @Inject constructor(
	private val oreDepositUseCases: OreDepositUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val toolUseCases: ToolUseCases,
	private val craftingStation: CraftingObjectUseCases,
	val savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _oreDepositId: String = checkNotNull(savedStateHandle[ORE_DEPOSIT_KEY])
	private val _oreDeposit = MutableStateFlow<OreDeposit?>(null)
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _materials = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _tools = MutableStateFlow<List<ItemTool>>(emptyList())
	private val _craftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())


	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState: StateFlow<OreDepositUiState> = combine(
		_oreDeposit,
		_biomes,
		_materials,
		_tools,
		_craftingStation,
		_isLoading,
		_error
	) { values ->
		OreDepositUiState(
			oreDeposit = values[0] as OreDeposit?,
			relatedBiomes = values[1] as List<Biome>,
			relatedMaterials = values[2] as List<MaterialDrop>,
			relatedTools = values[3] as List<ItemTool>,
			craftingStation = values[4] as List<CraftingObject>,
			isLoading = values[5] as Boolean,
			error = values[6] as String?,
		)

	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = OreDepositUiState()
	)

	init {
		loadOreDepositDetail()
	}


	internal fun loadOreDepositDetail() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				_error.value = null


				val oreDepositDeferred = async {
					oreDepositUseCases.getOreDepositByIdUseCase(_oreDepositId).first()
				}


				val relationObjectsDeferred = async {
					relationUseCases.getRelatedIdsUseCase(_oreDepositId).first()
				}


				val oreDeposit = oreDepositDeferred.await()
				val relationObjects = relationObjectsDeferred.await()

				_oreDeposit.value = oreDeposit

				val relationIds = relationObjects.map { it.id }


				val biomesDeferred = async {
					biomeUseCases.getBiomesByIdsUseCase(relationIds).first()
				}

				val materialsDeferred = async {
					val materials = materialUseCases.getMaterialsByIds(relationIds).first()
					val tempList = mutableListOf<MaterialDrop>()
					val relatedItemsMap = relationObjects.associateBy { it.id }

					for (material in materials) {
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf<Int?>(relatedItem?.quantity)
						val chanceStarList = listOf(relatedItem?.chance1star)
						tempList.add(
							MaterialDrop(
								itemDrop = material,
								quantityList = quantityList,
								chanceStarList = chanceStarList
							)
						)
					}
					tempList.sortedBy { it.itemDrop.order }
				}

				val toolsDeferred = async {
					toolUseCases.getToolsByIdsUseCase(relationIds).first().sortedBy { it.order }
				}
				val craftingStationsDeferred = async {
					craftingStation.getCraftingObjectsByIds(relationIds).first()
						.sortedBy { it.order }
				}

				_biomes.value = biomesDeferred.await()
				_materials.value = materialsDeferred.await()
				_tools.value = toolsDeferred.await()
				_craftingStation.value = craftingStationsDeferred.await()

			} catch (e: Exception) {
				Log.e("OreDepositViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}
}