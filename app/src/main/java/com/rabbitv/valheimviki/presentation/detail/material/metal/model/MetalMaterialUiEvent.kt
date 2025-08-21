package com.rabbitv.valheimviki.presentation.detail.material.metal.model

sealed class MetalMaterialUiEvent {
	data object ToggleFavorite : MetalMaterialUiEvent()
}