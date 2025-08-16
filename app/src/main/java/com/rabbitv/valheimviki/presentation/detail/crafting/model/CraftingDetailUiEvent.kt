package com.rabbitv.valheimviki.presentation.detail.crafting.model

sealed class CraftingDetailUiEvent {
	data object ToggleFavorite : CraftingDetailUiEvent()
}

