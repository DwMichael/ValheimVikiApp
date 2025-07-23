package com.rabbitv.valheimviki.presentation.favorite.model

import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class FavoriteUiState(
	val favoritesState: UIState<List<Favorite>> = UIState.Loading,
	val selectedCategory: AppCategory? = null,
)
