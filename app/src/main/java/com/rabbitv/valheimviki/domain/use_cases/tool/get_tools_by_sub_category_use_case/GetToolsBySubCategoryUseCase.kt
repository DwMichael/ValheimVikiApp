package com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory

class GetToolsBySubCategoryUseCase {
	operator fun invoke(
		itemTools: List<GameTool>,
		subCategory: ToolSubCategory
	): List<GameTool> {
		return itemTools.filter { it.subCategory == subCategory.toString() }
	}
}