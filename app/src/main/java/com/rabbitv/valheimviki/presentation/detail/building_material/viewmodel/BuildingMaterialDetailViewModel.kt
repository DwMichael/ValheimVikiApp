package com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiState
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
	private val relationUseCases: RelationUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _buildingMaterialId: String =
		checkNotNull(savedStateHandle[BUILDING_MATERIAL_DETAIL_KEY])
	private val _buildingMaterial = MutableStateFlow<BuildingMaterial?>(null)
	private val _materials = MutableStateFlow<List<Material>>(emptyList())
	private val _requiredCraftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_buildingMaterial,
		_materials,
		_requiredCraftingStation,
		_isLoading,
		_error
	) { buildingMaterial, materials, requiredCraftingStation, isLoading, error ->
		BuildingMaterialUiState(
			buildingMaterial = buildingMaterial,
			materials = materials,
			craftingStation = requiredCraftingStation,
			isLoading = isLoading,
			error = error
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
					materialUseCases.getMaterialsByIds(relationsIds).first()
				}




				_materials.value = materialsDeferred.await()
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