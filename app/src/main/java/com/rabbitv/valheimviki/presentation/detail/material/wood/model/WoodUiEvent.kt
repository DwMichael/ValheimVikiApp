package com.rabbitv.valheimviki.presentation.detail.material.wood.model

sealed class WoodUiEvent {
	data object ToggleFavorite : WoodUiEvent()
}