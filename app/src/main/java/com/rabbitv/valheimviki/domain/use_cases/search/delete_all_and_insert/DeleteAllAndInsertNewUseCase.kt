package com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert

import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import javax.inject.Inject


class DeleteAllAndInsertNewUseCase @Inject constructor(
	private val searchRepository: SearchRepository
) {
	suspend operator fun invoke(searchData: List<Search>) =
		searchRepository.deleteAllAndInsertNew(searchData)
}