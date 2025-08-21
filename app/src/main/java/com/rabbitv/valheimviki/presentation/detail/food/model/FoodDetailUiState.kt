package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class FoodDetailUiState(
	val food: Food? = null,
	val craftingCookingStation: CraftingObject? = null,
	val foodForRecipe: UIState< List<RecipeFoodData>> = UIState.Loading,
	val materialsForRecipe: UIState<List<RecipeMaterialData>> = UIState.Loading,
	val isFavorite: Boolean = false,
)
