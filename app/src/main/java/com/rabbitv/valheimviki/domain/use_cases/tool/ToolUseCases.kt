package com.rabbitv.valheimviki.domain.use_cases.tool

import com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case.GetLocalToolsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tool_by_id.GetToolByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_ids.GetToolsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case.GetToolsBySubCategoryUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class ToolUseCases @Inject constructor(
	val getLocalToolsUseCase: GetLocalToolsUseCase,
	val getToolByIdUseCase: GetToolByIdUseCase,
	val getToolsByIdsUseCase: GetToolsByIdsUseCase,
	val getToolsBySubCategoryUseCase: GetToolsBySubCategoryUseCase,
)