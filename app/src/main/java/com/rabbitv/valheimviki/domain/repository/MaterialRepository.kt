package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.material.Material
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MaterialRepository {
    fun getLocalMaterials(): Flow<List<Material>>
    fun getMaterialsByIds(ids: List<String>): List<Material>
    fun getMaterialById(id: String): Material?
    fun getMaterialsBySubCategory(subCategory: String): List<Material>
    fun getMaterialsBySubCategoryAndSubType(subCategory: String, subType: String): List<Material>
    suspend fun insertMaterials(materials: List<Material>)
    suspend fun fetchMaterials(lang: String): Response<List<Material>>
}