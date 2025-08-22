package com.rabbitv.valheimviki.presentation.detail.material.valuable.model

sealed class ValuableMaterialUiEvent {
	data object ToggleFavorite : ValuableMaterialUiEvent()
}