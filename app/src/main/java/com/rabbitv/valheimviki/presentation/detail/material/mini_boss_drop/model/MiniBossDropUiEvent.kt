package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model

sealed class MiniBossDropUiEvent {
	data object ToggleFavorite : MiniBossDropUiEvent()
}