package com.rabbitv.valheimviki.presentation.detail.building_material.model

sealed class BuildingMaterialUiEvent {
	data object ToggleFavorite : BuildingMaterialUiEvent()

}