package com.rabbitv.valheimviki.presentation.favorite.model

import com.rabbitv.valheimviki.domain.model.favorite.Favorite

data class UiStateFavorite(
	val favorites: List<Favorite> = emptyList(),
	val selectedCategory: FavoriteCategory? = null,
)
