package com.rabbitv.valheimviki.presentation.detail.ore_deposit.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiEvent
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiState
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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
	private val favoriteUseCases: FavoriteUseCases,
	val savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _oreDepositId: String = savedStateHandle.toRoute<WorldDetailDestination.OreDepositDetail>().oreDepositId
	private val _oreDeposit = MutableStateFlow<OreDeposit?>(null)
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _materials = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _tools = MutableStateFlow<List<ItemTool>>(emptyList())
	private val _craftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)
	private val _isFavorite = favoriteUseCases.isFavorite(_oreDepositId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	val uiState: StateFlow<OreDepositUiState> = combine(
		_oreDeposit,
		_biomes,
		_materials,
		_tools,
		_craftingStation,
		_isFavorite,
		_isLoading,
		_error
	) { oredDeposit,biomes, materials,tools,craftingStation,isFavorite ,isLoading, error ->
		OreDepositUiState(
			oreDeposit = oredDeposit,
			relatedBiomes = biomes,
			relatedMaterials = materials,
			relatedTools = tools,
			craftingStation = craftingStation,
			isFavorite = isFavorite,
			isLoading = isLoading,
			error = error
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
		viewModelScope.launch{
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

					materials.forEach {  material ->
						val relatedItem = relatedItemsMap[material.id]
						tempList.add(
							MaterialDrop(
								itemDrop = material,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = listOf(relatedItem?.chance1star)
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

	fun uiEvent(event: OreDepositUiEvent) {
		when (event) {
			OreDepositUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_oreDeposit.value?.let { bM ->
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