package com.rabbitv.valheimviki.domain.repository

import androidx.paging.PagingData
import com.rabbitv.valheimviki.domain.model.search.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

	fun getPagedSearchObjects(query: String, pageSize: Int): Flow<PagingData<Search>>


	suspend fun deleteAllAndInsertNew(searchData: List<Search>)
}