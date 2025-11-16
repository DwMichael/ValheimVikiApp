package com.rabbitv.valheimviki.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rabbitv.valheimviki.data.local.dao.SearchDao
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val searchDao: SearchDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchRepository {

	override fun getPagedSearchObjects(
		query: String,
		pageSize: Int
	): Flow<PagingData<Search>> = Pager(
		config = PagingConfig(
			pageSize = pageSize,
			prefetchDistance = pageSize / 2,
			enablePlaceholders = false
		),
		pagingSourceFactory = if (query.isBlank()) {
			{ searchDao.pagingSourceAll() }
		} else {
			{ searchDao.pagingSource(query.trim()) }
		}
	).flow.flowOn(ioDispatcher)

	override fun getSearchData(): Flow<List<Search>> {
		return searchDao.getAllSearch()
	}

	override suspend fun deleteAllAndInsertNew(searchData: List<Search>) {
		return withContext(ioDispatcher) {
			searchDao.deleteAllAndInsertNew(searchData)
		}
	}
}