package com.rabbitv.valheimviki.presentation.detail.trinket.model


import com.rabbitv.valheimviki.domain.model.favorite.Favorite

sealed class TrinketDetailUiEvent {
	data class ToggleFavorite(val favorite: Favorite) :
		TrinketDetailUiEvent()

}
