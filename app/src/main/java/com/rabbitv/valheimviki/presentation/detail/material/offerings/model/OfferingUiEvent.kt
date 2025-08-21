package com.rabbitv.valheimviki.presentation.detail.material.offerings.model

sealed class OfferingUiEvent {
	data object ToggleFavorite : OfferingUiEvent()
}