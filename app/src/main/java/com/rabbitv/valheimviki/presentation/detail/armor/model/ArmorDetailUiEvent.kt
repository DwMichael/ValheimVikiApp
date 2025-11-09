package com.rabbitv.valheimviki.presentation.detail.armor.model


sealed class ArmorDetailUiEvent {
	data object ToggleFavorite : ArmorDetailUiEvent()
}
