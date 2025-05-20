package com.rabbitv.valheimviki.presentation.tool.model

import com.example.domain.entities.tool.Tool
import com.rabbitv.valheimviki.domain.model.tool.ToolSubCategory

data class ToolListUiState(
    val toolList: List<Tool> = emptyList(),
    val selectedChip: ToolSubCategory? = null,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
