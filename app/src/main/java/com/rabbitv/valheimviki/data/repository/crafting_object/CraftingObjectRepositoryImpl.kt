package com.rabbitv.valheimviki.data.repository.crafting_object

import com.rabbitv.valheimviki.data.local.dao.CraftingObjectDao
import com.rabbitv.valheimviki.data.remote.api.ApiCraftingService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class CraftingObjectRepositoryImpl @Inject constructor(
	private val apiService: ApiCraftingService,
	private val craftingObjectDao: CraftingObjectDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CraftingObjectRepository {
	override suspend fun fetchCraftingObject(lang: String): Response<List<CraftingObject>> {
		return withContext(ioDispatcher) {
			apiService.fetchCraftingStations(lang)
		}
	}

	override fun getLocalCraftingObjects(): Flow<List<CraftingObject>> {
		return craftingObjectDao.getLocalCraftingObjects().flowOn(ioDispatcher)
	}

	override fun getCraftingObjectsByIds(ids: List<String>): Flow<List<CraftingObject>> {
		return craftingObjectDao.getCraftingObjectsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getCraftingObjectByIds(ids: List<String>): Flow<CraftingObject?> {
		return craftingObjectDao.getCraftingObjectByIds(ids).flowOn(ioDispatcher)
	}

	override fun getCraftingObjectById(id: String): Flow<CraftingObject?> {
		return craftingObjectDao.getCraftingObjectById(id).flowOn(ioDispatcher)
	}

	override fun getCraftingObjectsBySubCategory(subCategory: String): Flow<List<CraftingObject>> {
		return craftingObjectDao.getCraftingObjectsBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override suspend fun insertCraftingObjects(craftingObjects: List<CraftingObject>) {
		check(craftingObjects.isNotEmpty()) { "Crafting Object list cannot be empty , cannot insert ${craftingObjects.size} Crafting Object" }
		withContext(ioDispatcher) {
			craftingObjectDao.insertCraftingObjects(craftingObjects)
		}

	}
}