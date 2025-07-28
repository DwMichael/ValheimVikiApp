package com.rabbitv.valheimviki.presentation.tool.model

import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory

sealed class ToolListUiEvent {
	data class CategorySelected(val category: ToolSubCategory?) : ToolListUiEvent()
}