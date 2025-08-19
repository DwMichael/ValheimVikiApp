package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model

sealed class AggressiveCreatureUiEvent {
	data object ToggleFavorite : AggressiveCreatureUiEvent()
}