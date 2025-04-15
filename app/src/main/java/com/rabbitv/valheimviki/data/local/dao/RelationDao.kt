package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.relation.Relation
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {
    @Query("SELECT * FROM relations")
    fun getLocalRelations(): Flow<List<Relation>>

    @Query("""
        SELECT CASE 
            WHEN mainItemId = :queryId THEN relatedItemId 
            WHEN relatedItemId = :queryId THEN mainItemId 
        END AS oppositeId
        FROM relations
        WHERE mainItemId = :queryId OR relatedItemId = :queryId
    """)
    fun getRelatedIds(queryId: String): List<String>

    @Query("SELECT relatedItemId FROM relations WHERE mainItemId = :queryId ")
    fun getRelatedId(queryId: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelations(relations: List<Relation>)
}