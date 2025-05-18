package com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case

import com.example.domain.entities.tool.Tool
import com.rabbitv.valheimviki.domain.model.tool.ToolSubCategory

class GetToolsBySubCategoryUseCase {
    operator fun invoke(tools: List<Tool>, subCategory: ToolSubCategory): List<Tool> {
        return tools.filter { it.subCategory == subCategory.toString() }
    }
}