package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.Relation
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {
	@Query("SELECT * FROM relations")
	fun getLocalRelations(): Flow<List<Relation>>

	@Query(
		"""
        SELECT CASE
            WHEN mainItemId = :queryId THEN relatedItemId
            WHEN relatedItemId = :queryId THEN mainItemId
        END AS id,
        quantity,
		relationType,
        quantity2star,
        quantity3star,
        quantity4star,
        chance1star,
        chance2star,
        chance3star
        FROM relations
        WHERE mainItemId = :queryId OR relatedItemId = :queryId
    """
	)
	fun getRelatedIds(queryId: String): Flow<List<RelatedItem>>

	@Query(
		"""
    SELECT
        relatedItemId AS id,
        quantity,
		relationType,
        quantity2star,
        quantity3star,
        quantity4star,
        chance1star,
        chance2star,
        chance3star
    FROM relations
    WHERE mainItemId = :queryId
    """
	)
	fun getRelatedIdsFor(queryId: String): Flow<List<RelatedItem>>


	@Query("SELECT relatedItemId FROM relations WHERE mainItemId = :queryId ")
	fun getRelatedId(queryId: String): Flow<String?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertRelations(relations: List<Relation>)
}