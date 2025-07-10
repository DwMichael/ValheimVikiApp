package com.rabbitv.valheimviki.domain.use_cases.search.search_by_description

import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchByDescriptionUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	operator fun invoke(query: String): Flow<List<Search>> =
		searchRepository.searchByDescription(query)
}