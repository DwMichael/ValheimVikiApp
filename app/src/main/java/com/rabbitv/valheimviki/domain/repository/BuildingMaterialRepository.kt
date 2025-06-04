package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BuildingMaterialRepository {
    fun getLocalBuildingMaterials(): Flow<List<BuildingMaterial>>
    fun getBuildingMaterialsByIds(ids: List<String>): Flow<List<BuildingMaterial>>
    fun getBuildingMaterialById(id: String): Flow<BuildingMaterial?>
    fun getBuildingMaterialsBySubCategory(subCategory: String): Flow<List<BuildingMaterial>>
    fun getBuildingMaterialsBySubCategoryAndSubType(
        subCategory: String,
        subType: String
    ): Flow<List<BuildingMaterial>>

    suspend fun insertBuildingMaterial(buildingMaterials: List<BuildingMaterial>)
    suspend fun fetchBuildingMaterial(lang: String): Response<List<BuildingMaterial>>
}