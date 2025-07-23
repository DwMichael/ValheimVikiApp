package com.rabbitv.valheimviki.data.repository.search

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
	override suspend fun countSearchObjectsByName(query: String): Int {
		return withContext(ioDispatcher) {
			searchDao.countSearchObjectsByName(query)
		}
	}

	override suspend fun countSearchObjects(): Int {
		return withContext(ioDispatcher) { searchDao.countSearchObjects() }
	}

	override fun getAllSearchObjects(limit: Int, offset: Int): Flow<List<Search>> {
		return searchDao.getAllSearchObjects(limit, offset).flowOn(ioDispatcher)
	}

	override fun searchByName(query: String, limit: Int, offset: Int): Flow<List<Search>> {
		return searchDao.searchByName(query, limit, offset).flowOn(ioDispatcher)
	}

	override fun searchByDescription(query: String, limit: Int, offset: Int): Flow<List<Search>> {
		return searchDao.searchByName(query, limit, offset).flowOn(ioDispatcher)
	}

	override fun searchByNameAndDescription(
		query: String,
		limit: Int,
		offset: Int
	): Flow<List<Search>> {
		return searchDao.searchByName(query, limit, offset).flowOn(ioDispatcher)
	}

	override suspend fun deleteAllAndInsertNew(searchData: List<Search>) {
		return withContext(ioDispatcher) {
			searchDao.deleteAllAndInsertNew(searchData)
		}
	}
}