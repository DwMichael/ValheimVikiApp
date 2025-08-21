package com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model

sealed class MobDropUiEvent {
	data object ToggleFavorite : MobDropUiEvent()
}