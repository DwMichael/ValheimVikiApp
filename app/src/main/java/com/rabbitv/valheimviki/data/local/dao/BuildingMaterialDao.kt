package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingMaterialDao {
    @Query("SELECT * FROM building_materials")
    fun getLocalBuildMaterial(): Flow<List<BuildingMaterial>>

    @Query("SELECT * FROM building_materials WHERE id IN (:ids)")
    fun getBuildMaterialByIds(ids: List<String>): List<BuildingMaterial>

    @Query("SELECT * FROM building_materials WHERE id = :id")
    fun getBuildMaterialById(id: String): BuildingMaterial?

    @Query("SELECT * FROM building_materials WHERE subCategory = :subCategory ")
    fun getBuildMaterialsBySubCategory(subCategory: String): List<BuildingMaterial>

    @Query("SELECT * FROM building_materials WHERE subCategory = :subCategory AND subType = :subType")
    fun getBuildMaterialsBySubCategoryAndSubType(
        subCategory: String,
        subType: String
    ): List<BuildingMaterial>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildMaterials(materials: List<BuildingMaterial>)
}