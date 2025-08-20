package com.rabbitv.valheimviki.presentation.detail.creature.npc.model

sealed class NpcDetailUIEvent {
	data object ToggleFavorite : NpcDetailUIEvent()
}