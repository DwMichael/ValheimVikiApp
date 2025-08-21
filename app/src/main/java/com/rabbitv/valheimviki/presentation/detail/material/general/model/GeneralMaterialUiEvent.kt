package com.rabbitv.valheimviki.presentation.detail.material.general.model

sealed class GeneralMaterialUiEvent {
	data object ToggleFavorite : GeneralMaterialUiEvent()
}