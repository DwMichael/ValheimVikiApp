package com.rabbitv.valheimviki.presentation.detail.material.seeds.model

sealed class SeedUiEvent {
	data object ToggleFavorite : SeedUiEvent()
}