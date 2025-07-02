package com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.building_material.model.RequiredFood
import com.rabbitv.valheimviki.presentation.detail.building_material.model.RequiredMaterial
import com.rabbitv.valheimviki.utils.Constants.BUILDING_MATERIAL_DETAIL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class BuildingMaterialDetailViewModel @Inject constructor(
	private val buildingMaterialUseCases: BuildMaterialUseCases,
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val foodUseCases: FoodUseCases,//TODO ADD new list of food needed for specific item to be constructed
	private val relationUseCases: RelationUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _buildingMaterialId: String =
		checkNotNull(savedStateHandle[BUILDING_MATERIAL_DETAIL_KEY])
	private val _buildingMaterial = MutableStateFlow<BuildingMaterial?>(null)
	private val _materials = MutableStateFlow<List<RequiredMaterial>>(emptyList())
	private val _foods = MutableStateFlow<List<RequiredFood>>(emptyList())
	private val _requiredCraftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_buildingMaterial,
		_materials,
		_foods,
		_requiredCraftingStation,
		_isLoading,
		_error
	) { values ->
		BuildingMaterialUiState(
			buildingMaterial = values[0] as BuildingMaterial?,
			materials = values[1] as List<RequiredMaterial>,
			foods = values[2] as List<RequiredFood>,
			craftingStation = values[3] as List<CraftingObject>,
			isLoading = values[4] as Boolean,
			error = values[5] as String?,
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		BuildingMaterialUiState()
	)

	init {
		loadGeneralDropData()
	}

	internal fun loadGeneralDropData() {

		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				_error.value = null


				val buildingMaterialDeferred = async {
					buildingMaterialUseCases.getBuildMaterialById(_buildingMaterialId).first()
				}


				val relationObjectsDeferred = async {
					relationUseCases.getRelatedIdsUseCase(_buildingMaterialId).first()
				}


				val buildingMaterial = buildingMaterialDeferred.await()
				val relationObjects = relationObjectsDeferred.await()

				_buildingMaterial.value = buildingMaterial

				val relationsIds = relationObjects.map { it.id }

				val relationProducesIds =
					relationObjects.filter { it.relationType == RelationType.PRODUCES.toString() }
						.map { it.id }


				val requiredCraftingStationDeferred = async {
					craftingUseCases.getCraftingObjectsByIds(relationProducesIds).first()
				}

				val materialsDeferred = async {
					val materials = materialUseCases.getMaterialsByIds(relationsIds).first()
					val tempList = mutableListOf<RequiredMaterial>()

					val relatedItemsMap = relationObjects.associateBy { it.id }
					for (material in materials) {
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf<Int?>(
							relatedItem?.quantity,
							relatedItem?.quantity2star,
							relatedItem?.quantity3star
						)
						val chanceStarList = listOf<Int?>(
							relatedItem?.chance1star,
							relatedItem?.chance2star,
							relatedItem?.chance3star
						)
						tempList.add(
							RequiredMaterial(
								itemDrop = material,
								quantityList = quantityList,
								chanceStarList = chanceStarList,
								droppableType = DroppableType.MATERIAL,
							)
						)
					}
					tempList
				}
				val foodDeferred = async {
					val food = foodUseCases.getFoodListByIdsUseCase(relationsIds).first()
					val tempList = mutableListOf<RequiredFood>()

					val relatedItemsMap = relationObjects.associateBy { it.id }
					for (meal in food) {
						val relatedItem = relatedItemsMap[meal.id]
						val quantityList = listOf<Int?>(
							relatedItem?.quantity,
							relatedItem?.quantity2star,
							relatedItem?.quantity3star
						)
						val chanceStarList = listOf<Int?>(
							relatedItem?.chance1star,
							relatedItem?.chance2star,
							relatedItem?.chance3star
						)
						tempList.add(
							RequiredFood(
								itemDrop = meal,
								quantityList = quantityList,
								chanceStarList = chanceStarList,
								droppableType = DroppableType.MATERIAL,
							)
						)

					}
					tempList
				}


				_materials.value = materialsDeferred.await()
				_foods.value = foodDeferred.await()
				_requiredCraftingStation.value = requiredCraftingStationDeferred.await()

			} catch (e: Exception) {
				Log.e("BuildingMaterialDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}

		}


	}
}