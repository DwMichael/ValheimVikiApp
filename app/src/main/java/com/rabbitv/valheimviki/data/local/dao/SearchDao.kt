package com.rabbitv.valheimviki.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rabbitv.valheimviki.domain.model.search.Search

@Dao
interface SearchDao {
	@Query(
		"""
           SELECT * FROM search 
           JOIN search_fts ON search_fts.name == search.name
           WHERE search_fts.name MATCH :query || '*' 
           ORDER BY search.name ASC
        """
	)
	fun pagingSource(query: String): PagingSource<Int, Search>

	@Query("SELECT * FROM search ORDER BY name ASC")
	fun pagingSourceAll(): PagingSource<Int, Search>

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