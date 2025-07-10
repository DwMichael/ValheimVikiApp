package com.rabbitv.valheimviki.data.repository.search

import com.rabbitv.valheimviki.data.local.dao.SearchDao
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val searchDao: SearchDao
) : SearchRepository {
	override fun getAllSearchObjects(): Flow<List<Search>> {
		return searchDao.getAllSearchObjects()
	}

	override fun searchByName(query: String): Flow<List<Search>> {
		return searchDao.searchByName(query)
	}

	override fun searchByDescription(query: String): Flow<List<Search>> {
		return searchDao.searchByDescription(query)
	}

	override fun searchByNameAndDescription(query: String): Flow<List<Search>> {
		return searchDao.searchByNameAndDescription(query)
	}

	override suspend fun deleteAllAndInsertNew(searchData: List<Search>) {
		searchDao.deleteAllAndInsertNew(searchData)
	}
}