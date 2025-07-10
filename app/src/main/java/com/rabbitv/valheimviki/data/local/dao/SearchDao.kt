package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.search.SearchFTS
import com.rabbitv.valheimviki.domain.model.search.SearchView
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

	@Query("SELECT * FROM search_view")
	fun getAllAppObjects(): Flow<List<SearchView>>

	@Query(
		"""
        SELECT * FROM search_fts 
        WHERE search_fts MATCH :query
        LIMIT :limit
    """
	)
	fun searchDirectFullText(query: String, limit: Int = 50): Flow<List<SearchFTS>>

	@Query("INSERT INTO search_fts(search_fts) VALUES('rebuild')")
	suspend fun rebuildFtsIndex()
}