package com.rabbitv.valheimviki.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

	@Query("SELECT * FROM tools")
	fun getLocalTools(): Flow<List<GameTool>>

	@Query("SELECT * FROM Tools where subCategory = :subCategory")
	fun getToolsBySubCategory(subCategory: String): Flow<List<GameTool>>

	@Query("SELECT * FROM Tools WHERE id = :id")
	fun getToolById(id: String): Flow<List<GameTool>>

	@Query("SELECT * FROM Tools WHERE id IN (:ids)")
	fun getToolsByIds(ids: List<String>): Flow<List<GameTool>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertTools(Tools: List<GameTool>)

}