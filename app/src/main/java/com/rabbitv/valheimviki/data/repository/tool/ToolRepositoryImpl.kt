package com.rabbitv.valheimviki.data.repository.tool

import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
	private val apiService: ApiToolService,
	private val toolDao: ToolDao,
) : ToolRepository {
	override suspend fun fetchTools(language: String): Response<List<GameTool>> {
		return apiService.fetchTools(language)
	}

	override fun getLocalTools(): Flow<List<GameTool>> {
		return toolDao.getLocalTools()
	}

	override fun getToolsBySubCategory(subCategory: String): Flow<List<GameTool>> {
		return toolDao.getToolsBySubCategory(subCategory)
	}

	override suspend fun insertTools(itemTools: List<GameTool>) {
		check(itemTools.isNotEmpty()) { "Tools list cannot be empty , cannot insert ${itemTools.size} tools" }
		return toolDao.insertTools(itemTools)
	}
}