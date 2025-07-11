package com.rabbitv.valheimviki.domain.use_cases.search.count_search_objects

import com.rabbitv.valheimviki.domain.repository.SearchRepository
import javax.inject.Inject

class CountSearchObjectsUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	suspend operator fun invoke(): Int = searchRepository.countSearchObjects()
}