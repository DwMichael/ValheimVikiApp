package com.rabbitv.valheimviki.presentation.detail.food.model

sealed class FoodDetailUiEvent {
	data object ToggleFavorite : FoodDetailUiEvent()
}