package com.rabbitv.valheimviki.data.repository.crafting_object

import com.rabbitv.valheimviki.data.local.dao.CraftingObjectDao
import com.rabbitv.valheimviki.data.remote.api.ApiCraftingService
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CraftingObjectRepositoryImpl @Inject constructor(
	private val apiService: ApiCraftingService,
	private val craftingObjectDao: CraftingObjectDao
) : CraftingObjectRepository {
	override suspend fun fetchCraftingObject(lang: String): Response<List<CraftingObject>> {
		return apiService.fetchCraftingStations(lang)
	}

	override fun getLocalCraftingObjects(): Flow<List<CraftingObject>> {
		return craftingObjectDao.getLocalCraftingObjects()
	}

	override fun getCraftingObjectsByIds(ids: List<String>): Flow<List<CraftingObject>> {
		return craftingObjectDao.getCraftingObjectsByIds(ids)
	}

	override fun getCraftingObjectByIds(ids: List<String>): Flow<CraftingObject?> {
		return craftingObjectDao.getCraftingObjectByIds(ids)
	}

	override fun getCraftingObjectById(id: String): Flow<CraftingObject?> {
		return craftingObjectDao.getCraftingObjectById(id)
	}

	override fun getCraftingObjectsBySubCategory(subCategory: String): Flow<List<CraftingObject>> {
		return craftingObjectDao.getCraftingObjectsBySubCategory(subCategory)
	}

	override suspend fun insertCraftingObjects(craftingObjects: List<CraftingObject>) {
		check(craftingObjects.isNotEmpty()) { "Crafting Object list cannot be empty , cannot insert ${craftingObjects.size} Crafting Object" }
		craftingObjectDao.insertCraftingObjects(craftingObjects)
	}
}