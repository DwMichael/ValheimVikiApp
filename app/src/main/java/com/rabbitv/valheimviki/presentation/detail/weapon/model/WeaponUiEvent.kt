package com.rabbitv.valheimviki.presentation.detail.weapon.model

sealed class WeaponUiEvent {
	data object ToggleFavorite: WeaponUiEvent()
}