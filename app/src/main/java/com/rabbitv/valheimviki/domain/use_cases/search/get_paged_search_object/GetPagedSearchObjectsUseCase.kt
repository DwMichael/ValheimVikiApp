package com.rabbitv.valheimviki.domain.use_cases.search.get_paged_search_object

import androidx.paging.PagingData
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedSearchObjectsUseCase @Inject constructor(
	private val repo: SearchRepository
) {
	operator fun invoke(query: String, pageSize: Int): Flow<PagingData<Search>> =
		repo.getPagedSearchObjects(query, pageSize)
}