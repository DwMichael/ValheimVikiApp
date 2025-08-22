package com.rabbitv.valheimviki.presentation.detail.tree.model

sealed class TreeUiEvent {
	data object ToggleFavorite: TreeUiEvent()
}