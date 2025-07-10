package com.rabbitv.valheimviki.domain.use_cases.search.get_all_search_objects


import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSearchObjectsUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	operator fun invoke(): Flow<List<Search>> = searchRepository.getAllSearchObjects()
}