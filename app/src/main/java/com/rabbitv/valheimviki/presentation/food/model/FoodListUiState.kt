package com.rabbitv.valheimviki.presentation.food.model

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class FoodListUiState(
	val foodState: UIState<List<Food>> = UIState.Loading,
	val selectedCategory: FoodSubCategory = FoodSubCategory.COOKED_FOOD,
)