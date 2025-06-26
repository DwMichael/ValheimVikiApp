package com.rabbitv.valheimviki.data.repository.tool

import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
	private val apiService: ApiToolService,
	private val toolDao: ToolDao,
) : ToolRepository {
	override suspend fun fetchTools(language: String): Response<List<ItemTool>> {
		return apiService.fetchTools(language)
	}

	override fun getToolById(id: String): Flow<ItemTool?> {
		return toolDao.getToolById(id)
	}

	override fun getToolsByIds(ids: List<String>): Flow<List<ItemTool>> {
		return toolDao.getToolsByIds(ids)
	}

	override fun getLocalTools(): Flow<List<ItemTool>> {
		return toolDao.getLocalTools()
	}

	override fun getToolsBySubCategory(subCategory: String): Flow<List<ItemTool>> {
		return toolDao.getToolsBySubCategory(subCategory)
	}

	override suspend fun insertTools(itemTools: List<ItemTool>) {
		check(itemTools.isNotEmpty()) { "Tools list cannot be empty , cannot insert ${itemTools.size} tools" }
		return toolDao.insertTools(itemTools)
	}
}