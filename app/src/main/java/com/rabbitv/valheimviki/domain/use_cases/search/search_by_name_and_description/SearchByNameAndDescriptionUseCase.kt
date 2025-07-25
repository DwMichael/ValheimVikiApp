package com.rabbitv.valheimviki.domain.use_cases.search.search_by_name_and_description

import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchByNameAndDescriptionUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	operator fun invoke(query: String, limit: Int, offset: Int): Flow<List<Search>> =
		searchRepository.searchByNameAndDescription(query, limit, offset)
}