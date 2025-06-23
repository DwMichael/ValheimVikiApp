package com.rabbitv.valheimviki.presentation.detail.mead.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiState
import com.rabbitv.valheimviki.presentation.detail.mead.model.RecipeMeadData
import com.rabbitv.valheimviki.utils.Constants.FOOD_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
class MeadDetailViewModel @Inject constructor(
	private val meadUseCases: MeadUseCases,
	private val foodUseCases: FoodUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _meadId: String = checkNotNull(savedStateHandle[FOOD_KEY])
	private val _mead = MutableStateFlow<Mead?>(null)
	private val _craftingCookingStation = MutableStateFlow<CraftingObject?>(null)
	private val _foodForRecipe = MutableStateFlow<List<RecipeFoodData>>(emptyList())
	private val _meadForRecipe = MutableStateFlow<List<RecipeMeadData>>(emptyList())
	private val _materialsForRecipe = MutableStateFlow<List<RecipeMaterialData>>(emptyList())
	private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
	private val _error: MutableStateFlow<String?> = MutableStateFlow(null)


	val uiState: StateFlow<MeadDetailUiState> = combine(
		_mead,
		_craftingCookingStation,
		_foodForRecipe,
		_meadForRecipe,
		_materialsForRecipe,
		_isLoading,
		_error
	) { values ->
		MeadDetailUiState(
			mead = values[0] as Mead?,
			craftingCookingStation = values[1] as CraftingObject?,
			foodForRecipe = values[2] as List<RecipeFoodData>,
			meadForRecipe = values[3] as List<RecipeMeadData>,
			materialsForRecipe = values[4] as List<RecipeMaterialData>,
			isLoading = values[5] as Boolean,
			error = values[6] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = MeadDetailUiState()
	)


	init {
		loadFoodData()
	}


	internal fun loadFoodData() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true


				launch { _mead.value = meadUseCases.getMeadByIdUseCase(_meadId).first() }

				val relatedObjects: List<RelatedItem> =
					relationUseCases.getRelatedIdsForUseCase(_meadId).first()

				val relatedIds = relatedObjects.map { it.id }


				val craftingDeferred = async {
					_craftingCookingStation.value =
						craftingObjectUseCases.getCraftingObjectByIds(relatedIds).first()

				}
				val foodDeferred = async {
					val foods = foodUseCases.getFoodListByIdsUseCase(relatedIds).first()

					val tempList = mutableListOf<RecipeFoodData>()
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					foods.forEach { food ->
						val relatedItem = relatedItemsMap[food.id]
						val quantityList = listOf(
							relatedItem?.quantity,
						)
						tempList.add(
							RecipeFoodData(
								itemDrop = food,
								quantityList = quantityList,
								chanceStarList = emptyList(),
							)
						)
					}
					_foodForRecipe.value = tempList
				}

				val meadDeferred = async {
					val meads = meadUseCases.getMeadsByIdsUseCase(relatedIds).first()

					val tempList = mutableListOf<RecipeMeadData>()
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					meads.forEach { mead ->
						val relatedItem = relatedItemsMap[mead.id]
						val quantityList = listOf(
							relatedItem?.quantity,
						)
						tempList.add(
							RecipeMeadData(
								itemDrop = mead,
								quantityList = quantityList,
								chanceStarList = emptyList(),
							)
						)
					}
					_meadForRecipe.value = tempList
				}

				val materialsDeferred = async {
					val materials = materialUseCases.getMaterialsByIds(relatedIds).first()

					val tempList = mutableListOf<RecipeMaterialData>()
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					materials.forEach { material ->
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf(
							relatedItem?.quantity,
						)
						tempList.add(
							RecipeMaterialData(
								itemDrop = material,
								quantityList = quantityList,
								chanceStarList = emptyList(),
							)
						)
					}
					_materialsForRecipe.value = tempList
				}

				awaitAll(craftingDeferred, foodDeferred,meadDeferred, materialsDeferred)

			} catch (e: Exception) {
				Log.e("General fetch error MeadDetailViewModel", e.message.toString())
				_isLoading.value = false
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}
}