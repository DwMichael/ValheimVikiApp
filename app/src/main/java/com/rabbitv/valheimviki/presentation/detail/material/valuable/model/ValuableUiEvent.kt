package com.rabbitv.valheimviki.presentation.detail.material.valuable.model

sealed class ValuableUiEvent {
	data object ToggleFavorite : ValuableUiEvent()
}