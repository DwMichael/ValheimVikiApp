package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.search.SearchFTS
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

	fun searchName(name: String): Flow<List<SearchFTS>>
}