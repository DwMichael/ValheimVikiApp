package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ToolRepository {
	suspend fun fetchTools(language: String): Response<List<ItemTool>>
	fun getToolById(id: String): Flow<ItemTool?>
	fun getToolsByIds(ids: List<String>): Flow<List<ItemTool>>
	fun getLocalTools(): Flow<List<ItemTool>>
	fun getToolsBySubCategory(subCategory: String): Flow<List<ItemTool>>
	suspend fun insertTools(itemTools: List<ItemTool>)
}