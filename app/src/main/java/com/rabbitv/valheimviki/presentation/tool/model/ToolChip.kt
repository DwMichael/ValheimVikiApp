package com.rabbitv.valheimviki.presentation.tool.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.presentation.components.chip.ChipData

class ToolChip(
	override val option: ToolSubCategory,
	override val icon: ImageVector,
	@get:androidx.annotation.StringRes override val labelRes: Int
) : ChipData<ToolSubCategory>