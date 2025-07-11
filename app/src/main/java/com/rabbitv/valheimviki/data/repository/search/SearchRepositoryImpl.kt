package com.rabbitv.valheimviki.data.repository.search

import com.rabbitv.valheimviki.data.local.dao.SearchDao
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val searchDao: SearchDao
) : SearchRepository {
	override suspend fun countSearchObjectsByName(query: String): Int {
		return searchDao.countSearchObjectsByName(query)
	}

	override suspend fun countSearchObjects(): Int {
		return searchDao.countSearchObjects()
	}

	override fun getAllSearchObjects(limit: Int, offset: Int): Flow<List<Search>> {
		return searchDao.getAllSearchObjects(limit, offset)
	}

	override fun searchByName(query: String, limit: Int, offset: Int): Flow<List<Search>> {
		return searchDao.searchByName(query, limit, offset)
	}

	override fun searchByDescription(query: String, limit: Int, offset: Int): Flow<List<Search>> {
//		return searchDao.searchByDescription(query, limit, offset)
		return searchDao.searchByName(query, limit, offset)
	}

	override fun searchByNameAndDescription(
		query: String,
		limit: Int,
		offset: Int
	): Flow<List<Search>> {
//		return searchDao.searchByNameAndDescription(query, limit, offset)
		return searchDao.searchByName(query, limit, offset)
	}

	override suspend fun deleteAllAndInsertNew(searchData: List<Search>) {
		searchDao.deleteAllAndInsertNew(searchData)
	}
}