package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.tree.Tree
import kotlinx.coroutines.flow.Flow

@Dao
interface TreeDao {
    @Query("SELECT * FROM trees")
    fun getLocalTrees(): Flow<List<Tree>>

    @Query("SELECT * FROM trees WHERE id = :id")
    fun getTreeById(id: String): Flow<Tree?>

    @Query("SELECT * FROM trees WHERE id IN (:ids)")
    fun getTreesByIds(ids: List<String>): Flow<List<Tree>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrees(trees: List<Tree>)

}