package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CraftingObjectRepository {
    suspend fun fetchCraftingObject(lang: String): Response<List<CraftingObject>>
    fun getLocalCraftingObjects(): Flow<List<CraftingObject>>
    fun getCraftingObjectsBySubCategory(subCategory: String): Flow<List<CraftingObject>>
    suspend fun insertCraftingObjects(craftingObjects: List<CraftingObject>)
}