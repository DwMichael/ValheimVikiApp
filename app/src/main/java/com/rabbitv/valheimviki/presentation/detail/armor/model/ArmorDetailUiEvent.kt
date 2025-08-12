package com.rabbitv.valheimviki.presentation.detail.armor.model


import com.rabbitv.valheimviki.domain.model.favorite.Favorite

sealed class ArmorDetailUiEvent {
	data class ToggleFavorite(val favorite: Favorite) :
		ArmorDetailUiEvent()

}
