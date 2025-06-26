package com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case

import com.rabbitv.valheimviki.domain.exceptions.MeadFetchLocalException
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalToolsUseCase @Inject constructor(
	private val toolRepository: ToolRepository
) {
	operator fun invoke(): Flow<List<ItemTool>> {
		return try {
			toolRepository.getLocalTools().map { tools ->
				tools.sortedBy { it.order }
			}
		} catch (e: Exception) {
			throw MeadFetchLocalException("Get local Tools encounter exception $e")
		}

	}
}