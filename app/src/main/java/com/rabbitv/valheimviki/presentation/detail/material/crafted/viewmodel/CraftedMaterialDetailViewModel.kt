package com.rabbitv.valheimviki.presentation.detail.material.crafted.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.crafted.model.CraftedMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft
import com.rabbitv.valheimviki.utils.Constants.CRAFTED_MATERIAL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class CraftedMaterialDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _materialId: String = checkNotNull(savedStateHandle[CRAFTED_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _requiredCraftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
	private val _relatedMaterial = MutableStateFlow<List<MaterialToCraft>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_material,
		_requiredCraftingStation,
		_relatedMaterial,
		favoriteUseCases.isFavorite(_materialId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		CraftedMaterialUiState(
			material = values[0] as Material?,
			requiredCraftingStations = values[1] as List<CraftingObject>,
			relatedMaterial = values[2] as List<MaterialToCraft>,
			isFavorite = values[3] as Boolean,
			isLoading = values[4] as Boolean,
			error = values[5] as String?
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		CraftedMaterialUiState()
	)

	init {
		loadCraftedMaterialDropData()
	}

	internal fun loadCraftedMaterialDropData() {

		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				_error.value = null


				val materialDeferred = async {
					materialUseCases.getMaterialById(_materialId).first()
				}


				val relationObjectsDeferred = async {
					relationUseCases.getRelatedIdsUseCase(_materialId).first()
				}

				val relationRightDeferred = async {
					relationUseCases.getRelatedIdsForUseCase(_materialId).first()
				}


				val material = materialDeferred.await()
				val relationObjects = relationObjectsDeferred.await()
				val relationObjectsRight = relationRightDeferred.await()

				_material.value = material

				val relationProducesIds =
					relationObjects.filter { it.relationType == RelationType.PRODUCES.toString() }
						.map { it.id }

				val relationIds = relationObjectsRight.map { it.id }

				val requiredCraftingStationDeferred = async {
					craftingUseCases.getCraftingObjectsByIds(relationProducesIds).first()
				}

				val materialsDeferred = async {
					val materials = materialUseCases.getMaterialsByIds(relationIds).first()

					val tempList = mutableListOf<MaterialToCraft>()
					val relatedItemsMap = relationObjects.associateBy { it.id }
					materials.forEach { material ->
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf<Int?>(
							relatedItem?.quantity,
							relatedItem?.quantity2star,
							relatedItem?.quantity3star
						)
						tempList.add(
							MaterialToCraft(
								material = material,
								quantityList = quantityList,
							)
						)
					}
					tempList
				}

				_requiredCraftingStation.value = requiredCraftingStationDeferred.await()
				_relatedMaterial.value = materialsDeferred.await()
			} catch (e: Exception) {
				Log.e("CraftedMaterialDetailViewModel", "General fetch error: ${e.message}", e)
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