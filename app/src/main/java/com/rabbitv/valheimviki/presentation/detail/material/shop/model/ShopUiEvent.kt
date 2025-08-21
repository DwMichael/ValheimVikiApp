package com.rabbitv.valheimviki.presentation.detail.material.shop.model

sealed class ShopUiEvent {
	data object ToggleFavorite : ShopUiEvent()
}