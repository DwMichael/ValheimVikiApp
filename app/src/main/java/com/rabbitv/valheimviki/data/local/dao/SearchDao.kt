package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rabbitv.valheimviki.domain.model.search.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

	@Query("SELECT * FROM search ORDER BY name ASC LIMIT :limit OFFSET :offset")
	fun getAllSearchObjects(limit: Int, offset: Int): Flow<List<Search>>

	@Query(
		"""
        SELECT * FROM search 
		JOIN search_fts ON search_fts.name == search.name
        WHERE search_fts.name MATCH :query
		ORDER BY search.name ASC LIMIT :limit OFFSET :offset
    """
	)
	fun searchByName(query: String, limit: Int, offset: Int): Flow<List<Search>>

	@Query(
		"""
        SELECT * FROM search 
		JOIN search_fts ON search_fts.description == search.description
        WHERE search_fts.description MATCH :query
		ORDER BY search.name ASC LIMIT :limit OFFSET :offset
    """
	)
	fun searchByDescription(query: String, limit: Int, offset: Int): Flow<List<Search>>

	@Query(
		"""
		SELECT DISTINCT search.* FROM search
		JOIN search_fts ON	(search_fts.name == search.name 
		AND search_fts.description == search.description)
		WHERE search_fts.name MATCH :query OR search_fts.description MATCH :query
		ORDER BY search.name ASC LIMIT :limit OFFSET :offset
    """
	)
	fun searchByNameAndDescription(query: String, limit: Int, offset: Int): Flow<List<Search>>

	@Query("DELETE FROM search")
	suspend fun deleteAllSearchData()

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun populateSearch(searchData: List<Search>)

	@Transaction
	suspend fun deleteAllAndInsertNew(searchData: List<Search>) {
		deleteAllSearchData()
		populateSearch(searchData)
	}
}