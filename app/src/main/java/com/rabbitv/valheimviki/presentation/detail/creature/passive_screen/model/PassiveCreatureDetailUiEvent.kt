package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model

sealed class PassiveCreatureDetailUiEvent {
	data object ToggleFavorite : PassiveCreatureDetailUiEvent()
}