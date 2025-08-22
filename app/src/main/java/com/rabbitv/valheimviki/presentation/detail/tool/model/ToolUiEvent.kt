package com.rabbitv.valheimviki.presentation.detail.tool.model

sealed class ToolUiEvent {
	data object ToggleFavorite: ToolUiEvent()
}