package com.rabbitv.valheimviki.domain.use_cases.search.search_by_name

import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchByNameUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	operator fun invoke(query: String, limit: Int, offset: Int): Flow<List<Search>> =
		searchRepository.searchByName(query, limit, offset)
}