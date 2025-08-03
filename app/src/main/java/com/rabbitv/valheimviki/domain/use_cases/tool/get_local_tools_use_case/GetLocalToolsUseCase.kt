package com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case

import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalToolsUseCase @Inject constructor(
	private val toolRepository: ToolRepository
) {
	operator fun invoke(): Flow<List<ItemTool>> = toolRepository.getLocalTools()
}