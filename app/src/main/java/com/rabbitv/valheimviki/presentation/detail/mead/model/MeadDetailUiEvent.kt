package com.rabbitv.valheimviki.presentation.detail.mead.model

sealed class MeadDetailUiEvent {
	data object ToggleFavorite : MeadDetailUiEvent()
}