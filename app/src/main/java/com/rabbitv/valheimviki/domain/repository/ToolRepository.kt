package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ToolRepository {
	suspend fun fetchTools(language: String): Response<List<GameTool>>
	fun getToolById(id: String): Flow<List<GameTool>>
	fun getToolsByIds(ids: List<String>): Flow<List<GameTool>>
	fun getLocalTools(): Flow<List<GameTool>>
	fun getToolsBySubCategory(subCategory: String): Flow<List<GameTool>>
	suspend fun insertTools(itemTools: List<GameTool>)
}