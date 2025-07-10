package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.search.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

	fun getAllSearchObjects(): Flow<List<Search>>
	fun searchByName(query: String): Flow<List<Search>>
	fun searchByDescription(query: String): Flow<List<Search>>
	fun searchByNameAndDescription(query: String): Flow<List<Search>>

	suspend fun deleteAllAndInsertNew(searchData: List<Search>)
}