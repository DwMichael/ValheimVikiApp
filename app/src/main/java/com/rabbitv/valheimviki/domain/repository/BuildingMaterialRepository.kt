package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BuildingMaterialRepository {
    fun getLocalBuildingMaterials(): Flow<List<BuildingMaterial>>
    fun getBuildingMaterialsByIds(ids: List<String>): List<BuildingMaterial>
    fun getBuildingMaterialById(id: String): BuildingMaterial?
    fun getBuildingMaterialsBySubCategory(subCategory: String): List<BuildingMaterial>
    fun getBuildingMaterialsBySubCategoryAndSubType(
        subCategory: String,
        subType: String
    ): List<BuildingMaterial>

    suspend fun insertBuildingMaterial(buildingMaterials: List<BuildingMaterial>)
    suspend fun fetchBuildingMaterial(lang: String): Response<List<BuildingMaterial>>
}