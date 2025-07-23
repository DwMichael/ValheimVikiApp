package com.rabbitv.valheimviki.data.repository.tool

import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
	private val apiService: ApiToolService,
	private val toolDao: ToolDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ToolRepository {
	override suspend fun fetchTools(language: String): Response<List<ItemTool>> {
		return withContext(ioDispatcher) { apiService.fetchTools(language) }
	}

	override fun getToolById(id: String): Flow<ItemTool?> {
		return toolDao.getToolById(id).flowOn(ioDispatcher)
	}

	override fun getToolsByIds(ids: List<String>): Flow<List<ItemTool>> {
		return toolDao.getToolsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getLocalTools(): Flow<List<ItemTool>> {
		return toolDao.getLocalTools().flowOn(ioDispatcher)
	}

	override fun getToolsBySubCategory(subCategory: String): Flow<List<ItemTool>> {
		return toolDao.getToolsBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override suspend fun insertTools(itemTools: List<ItemTool>) {
		check(itemTools.isNotEmpty()) { "Tools list cannot be empty , cannot insert ${itemTools.size} tools" }
		return withContext(ioDispatcher) {
			toolDao.insertTools(itemTools)
		}
	}
}