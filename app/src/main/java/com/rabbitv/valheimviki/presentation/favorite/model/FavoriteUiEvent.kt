package com.rabbitv.valheimviki.presentation.favorite.model


import com.rabbitv.valheimviki.domain.model.category.AppCategory


sealed class FavoriteUiEvent {
	data class CategorySelected(val category: AppCategory?) : FavoriteUiEvent()
}