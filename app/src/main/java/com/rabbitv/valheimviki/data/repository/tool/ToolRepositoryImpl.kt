package com.rabbitv.valheimviki.data.repository.tool

import com.example.domain.entities.tool.Tool
import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
    private val apiService: ApiToolService,
    private val toolDao: ToolDao,
) : ToolRepository {
    override suspend fun fetchTools(language: String): Response<List<Tool>> {
        return apiService.fetchTools(language)
    }

    override fun getLocalTools(): Flow<List<Tool>> {
        return toolDao.getLocalTools()
    }

    override fun getToolsBySubCategory(subCategory: String): Flow<List<Tool>> {
        return toolDao.getToolsBySubCategory(subCategory)
    }

    override suspend fun insertTools(tools: List<Tool>) {
        check(tools.isNotEmpty()) { "Tools list cannot be empty , cannot insert ${tools.size} tools" }
        return toolDao.insertTools(tools)
    }
}