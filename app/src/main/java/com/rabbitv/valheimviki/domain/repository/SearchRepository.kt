package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.search.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
	suspend fun countSearchObjectsByName(query: String): Int
	suspend fun countSearchObjects(): Int
	fun getAllSearchObjects(limit: Int, offset: Int): Flow<List<Search>>
	fun searchByName(query: String, limit: Int, offset: Int): Flow<List<Search>>
	fun searchByDescription(query: String, limit: Int, offset: Int): Flow<List<Search>>
	fun searchByNameAndDescription(query: String, limit: Int, offset: Int): Flow<List<Search>>

	suspend fun deleteAllAndInsertNew(searchData: List<Search>)
}