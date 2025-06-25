package com.rabbitv.valheimviki.domain.use_cases.tool.get_tool_by_id

import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetToolByIdUseCase @Inject constructor(
	private val toolRepository: ToolRepository
) {
	operator fun invoke(id: String): Flow<List<GameTool>> = toolRepository.getToolById(id)

}
