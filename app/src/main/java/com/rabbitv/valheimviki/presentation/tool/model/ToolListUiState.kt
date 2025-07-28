package com.rabbitv.valheimviki.presentation.tool.model

import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class ToolListUiState(
	val toolState: UIState<List<ItemTool>> = UIState.Loading,
	val selectedCategory: ToolSubCategory? = null,
)