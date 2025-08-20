package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model

import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureUiEvent

sealed class MainBossUiEvent {
	data object ToggleFavorite : MainBossUiEvent()
}