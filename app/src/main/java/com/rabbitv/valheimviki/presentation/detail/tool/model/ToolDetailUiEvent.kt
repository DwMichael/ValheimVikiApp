package com.rabbitv.valheimviki.presentation.detail.tool.model

sealed class ToolDetailUiEvent {
	data object ToggleFavorite: ToolDetailUiEvent()
}