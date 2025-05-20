package com.rabbitv.valheimviki.domain.use_cases.tool

import com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case.GetLocalToolsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case.GetToolsBySubCategoryUseCase

data class ToolUseCases(
    val getLocalToolsUseCase: GetLocalToolsUseCase,
    val getToolsBySubCategoryUseCase: GetToolsBySubCategoryUseCase,
)