package com.rabbitv.valheimviki.domain.repository

import com.example.domain.entities.tool.Tool
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ToolRepository {
    suspend fun fetchTools(language: String): Response<List<Tool>>
    fun getLocalTools(): Flow<List<Tool>>
    fun getToolsBySubCategory(subCategory: String): Flow<List<Tool>>
    suspend fun insertTools(tools: List<Tool>)
}