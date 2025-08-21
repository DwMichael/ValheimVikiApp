package com.rabbitv.valheimviki.presentation.detail.material.gemstones.model

sealed class GemstoneUiEvent {
	data object ToggleFavorite : GemstoneUiEvent()
}