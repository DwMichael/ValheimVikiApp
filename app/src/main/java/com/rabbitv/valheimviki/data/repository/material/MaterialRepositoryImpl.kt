package com.rabbitv.valheimviki.data.repository.material

import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class MaterialRepositoryImpl @Inject constructor(
	private val apiService: ApiMaterialsService,
	private val materialDao: MaterialDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MaterialRepository {
	override fun getLocalMaterials(): Flow<List<Material>> {
		return materialDao.getLocalMaterials().flowOn(ioDispatcher)
	}

	override fun getMaterialsByIds(ids: List<String>): Flow<List<Material>> {
		return materialDao.getMaterialsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getMaterialById(id: String): Flow<Material?> {
		return materialDao.getMaterialById(id).flowOn(ioDispatcher)
	}

	override fun getMaterialsBySubCategory(subCategory: String): Flow<List<Material>> {
		return materialDao.getMaterialsBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override fun getMaterialsByCategoryAndIds(
		subCategory: String,
		ids: List<String>
	): Flow<List<Material>> {
		return materialDao.getMaterialsByIdsAndCategory(subCategory,ids).flowOn(ioDispatcher)
	}

	override fun getMaterialsBySubCategoryAndSubType(
		subCategory: String,
		subType: String,
	): Flow<List<Material>> {
		return materialDao.getMaterialsBySubCategoryAndSubType(subCategory, subType)
			.flowOn(ioDispatcher)
	}

	override suspend fun insertMaterials(materials: List<Material>) {
		check(materials.isNotEmpty(), { "Materials cannot be empty" })
		withContext(ioDispatcher) {
			materialDao.insertMaterial(materials)
		}
	}

	override suspend fun fetchMaterials(lang: String): Response<List<Material>> {
		return withContext(ioDispatcher) {
			apiService.fetchMaterials(lang)
		}
	}


}