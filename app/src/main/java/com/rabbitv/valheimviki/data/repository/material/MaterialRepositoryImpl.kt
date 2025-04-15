package com.rabbitv.valheimviki.data.repository.material

import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MaterialRepositoryImpl @Inject constructor(
    private val apiService : ApiMaterialsService,
    private val materialDao: MaterialDao
) : MaterialRepository {
    override fun getLocalMaterials(): Flow<List<Material>> {
        return materialDao.getLocalMaterials()
    }

    override fun getMaterialsByIds(ids: List<String>): List<Material> {
        return materialDao.getMaterialsByIds(ids)
    }

    override fun getMaterialById(id: String): Material? {
        return materialDao.getMaterialById(id)
    }

    override fun getMaterialsBySubCategory(subCategory: String): List<Material> {
        return materialDao.getMaterialsBySubCategory(subCategory)
    }

    override fun getMaterialsBySubCategoryAndSubType(
        subCategory: String,
        subType: String,
    ): List<Material> {
        return materialDao.getMaterialsBySubCategoryAndSubType(subCategory, subType)
    }

    override suspend fun insertMaterials(materials: List<Material>) {
        check(materials.isNotEmpty(),{"Materials cannot be empty"})
        return materialDao.insertMaterial(materials)
    }

    override suspend fun fetchMaterials(lang:String): Response<List<Material>> {
        return apiService.fetchMaterials(lang)
    }


}