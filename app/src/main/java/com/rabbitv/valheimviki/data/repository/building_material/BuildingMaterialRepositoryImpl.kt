package com.rabbitv.valheimviki.data.repository.building_material

import com.rabbitv.valheimviki.data.local.dao.BuildingMaterialDao
import com.rabbitv.valheimviki.data.remote.api.ApiBuildingMaterialService
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class BuildingMaterialRepositoryImpl @Inject constructor(
    private val apiService: ApiBuildingMaterialService,
    private val buildingMaterialDao: BuildingMaterialDao
) : BuildingMaterialRepository {
    override fun getLocalBuildingMaterials(): Flow<List<BuildingMaterial>> {
        return buildingMaterialDao.getLocalBuildMaterial()
    }

    override fun getBuildingMaterialsByIds(ids: List<String>): Flow<List<BuildingMaterial>> {
        return buildingMaterialDao.getBuildMaterialByIds(ids)
    }

    override fun getBuildingMaterialById(id: String): Flow<BuildingMaterial?> {
        return buildingMaterialDao.getBuildMaterialById(id)
    }

    override fun getBuildingMaterialsBySubCategory(subCategory: String): Flow<List<BuildingMaterial>> {
        return buildingMaterialDao.getBuildMaterialsBySubCategory(subCategory)
    }

    override fun getBuildingMaterialsBySubCategoryAndSubType(
        subCategory: String,
        subType: String
    ): Flow<List<BuildingMaterial>> {
        return buildingMaterialDao.getBuildMaterialsBySubCategoryAndSubType(subCategory, subType)
    }

    override suspend fun insertBuildingMaterial(buildingMaterials: List<BuildingMaterial>) {
        check(buildingMaterials.isNotEmpty()) { "buildingMaterials list cannot be empty , cannot insert ${buildingMaterials.size} weapons" }
        return buildingMaterialDao.insertBuildMaterials(buildingMaterials)
    }

    override suspend fun fetchBuildingMaterial(lang: String): Response<List<BuildingMaterial>> {
        return apiService.fetchBuildingMaterial(lang)
    }
}