package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food

data class FoodDetailUiState(
	val food: Food? = null,
	val craftingCookingStation: CraftingObject? = null,
	val foodForRecipe: List<RecipeFoodData> = emptyList(),
	val materialsForRecipe: List<RecipeMaterialData> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)
