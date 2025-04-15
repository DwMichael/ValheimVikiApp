package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.material.Material
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

 @Query("SELECT * FROM materials")
 fun getLocalMaterials(): Flow<List<Material>>

 @Query("SELECT * FROM materials WHERE id IN (:ids)")
 fun getMaterialsByIds(ids: List<String>): List<Material>

 @Query("SELECT * FROM materials WHERE category = 'MATERIAL' AND id = :id")
 fun getMaterialById(id: String): Material?

 @Query("SELECT * FROM materials WHERE category = 'MATERIAL' AND subCategory = :subCategory ")
 fun getMaterialsBySubCategory(subCategory: String): List<Material>

 @Query("SELECT * FROM materials WHERE category = 'MATERIAL' AND subCategory = :subCategory AND subType = :subType")
 fun getMaterialsBySubCategoryAndSubType(subCategory: String, subType: String): List<Material>

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertMaterial(materials: List<Material>)

}