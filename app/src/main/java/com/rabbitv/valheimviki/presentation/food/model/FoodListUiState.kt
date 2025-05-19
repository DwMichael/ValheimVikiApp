package com.rabbitv.valheimviki.presentation.food.model

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory

data class FoodListUiState(
    val foodList: List<Food> = emptyList(),
    val selectedSubCategory: FoodSubCategory = FoodSubCategory.COOKED_FOOD,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
