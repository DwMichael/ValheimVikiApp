package com.rabbitv.valheimviki.presentation.detail.material.crafted.model

sealed class CraftingMaterialUiEvent {
	data object ToggleFavorite : CraftingMaterialUiEvent()
}