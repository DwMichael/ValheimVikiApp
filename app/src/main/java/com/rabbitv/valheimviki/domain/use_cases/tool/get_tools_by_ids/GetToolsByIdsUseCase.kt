package com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_ids

import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetToolsByIdsUseCase @Inject constructor(
	private val toolRepository: ToolRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<GameTool>> = toolRepository.getToolsByIds(ids)

}
