package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.tool.Tool
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Query("SELECT * FROM tools")
    fun getLocalTools(): Flow<List<Tool>>

    @Query("SELECT * FROM Tools where subCategory = :subCategory")
    fun getToolsBySubCategory(subCategory: String): List<Tool>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTools(Tools: List<Tool>)

}