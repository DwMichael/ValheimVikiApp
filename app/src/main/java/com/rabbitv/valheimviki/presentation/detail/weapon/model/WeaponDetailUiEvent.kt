package com.rabbitv.valheimviki.presentation.detail.weapon.model

sealed class WeaponDetailUiEvent {
	data object ToggleFavorite: WeaponDetailUiEvent()
}