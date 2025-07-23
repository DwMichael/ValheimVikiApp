package com.rabbitv.valheimviki.presentation.food.model

import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory

sealed class FoodListUiEvent {
	data class CategorySelected(val category: FoodSubCategory) : FoodListUiEvent()
}