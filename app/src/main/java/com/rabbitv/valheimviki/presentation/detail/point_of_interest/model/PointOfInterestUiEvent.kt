package com.rabbitv.valheimviki.presentation.detail.point_of_interest.model

sealed class PointOfInterestUiEvent {
	data object ToggleFavorite: PointOfInterestUiEvent()
}