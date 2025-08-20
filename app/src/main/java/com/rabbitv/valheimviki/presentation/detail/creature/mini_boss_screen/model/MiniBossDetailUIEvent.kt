package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model

sealed class MiniBossDetailUIEvent {
	data object ToggleFavorite : MiniBossDetailUIEvent()
}