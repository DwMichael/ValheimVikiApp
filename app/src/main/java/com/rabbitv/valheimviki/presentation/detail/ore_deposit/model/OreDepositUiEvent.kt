package com.rabbitv.valheimviki.presentation.detail.ore_deposit.model

sealed class OreDepositUiEvent {
	data object ToggleFavorite : OreDepositUiEvent()
}