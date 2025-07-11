package com.rabbitv.valheimviki.domain.use_cases.search.count_search_objects_by_name

import com.rabbitv.valheimviki.domain.repository.SearchRepository
import javax.inject.Inject

class CountSearchObjectsByNameUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	suspend operator fun invoke(query: String): Int =
		searchRepository.countSearchObjectsByName(query)
}