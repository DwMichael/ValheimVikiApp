package com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model

sealed class BossDropUiEvent {
	data object ToggleFavorite : BossDropUiEvent()
}