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

	@Query("SELECT * FROM search")
	fun getAllSearchObjects(): Flow<List<Search>>

	@Query(
		"""
        SELECT * FROM search 
		JOIN search_fts ON search_fts.name == search.name
        WHERE search_fts.name MATCH :query
    """
	)
	fun searchByName(query: String): Flow<List<Search>>

	@Query(
		"""
        SELECT * FROM search 
		JOIN search_fts ON search_fts.description == search.description
        WHERE search_fts.description MATCH :query
    """
	)
	fun searchByDescription(query: String): Flow<List<Search>>

	@Query(
		"""
		SELECT DISTINCT search.* FROM search
		JOIN search_fts ON	(search_fts.name == search.name 
		AND search_fts.description == search.description)
		WHERE search_fts.name MATCH :query OR search_fts.description MATCH :query
    """
	)
	fun searchByNameAndDescription(query: String): Flow<List<Search>>

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