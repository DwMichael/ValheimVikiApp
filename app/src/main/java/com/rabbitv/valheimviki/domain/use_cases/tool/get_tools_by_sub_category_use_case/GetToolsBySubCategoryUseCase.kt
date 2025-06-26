package com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory

class GetToolsBySubCategoryUseCase {
	operator fun invoke(
		itemTools: List<ItemTool>,
		subCategory: ToolSubCategory
	): List<ItemTool> {
		return itemTools.filter { it.subCategory == subCategory.toString() }
	}
}