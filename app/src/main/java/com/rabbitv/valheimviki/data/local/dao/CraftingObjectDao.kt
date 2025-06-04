package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import kotlinx.coroutines.flow.Flow

@Dao
interface CraftingObjectDao {
    @Query("SELECT * FROM crafting_objects")
    fun getLocalCraftingObjects(): Flow<List<CraftingObject>>

    @Query("SELECT * FROM crafting_objects where subCategory = :subCategory")
    fun getCraftingObjectsBySubCategory(subCategory: String): Flow<List<CraftingObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCraftingObjects(craftingObjects: List<CraftingObject>)

}