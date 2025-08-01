package com.rabbitv.valheimviki.domain.use_cases.search

import com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert.DeleteAllAndInsertNewUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.get_paged_search_object.GetPagedSearchObjectsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class SearchUseCases @Inject constructor(
	val deleteAllAndInsertNewUseCase: DeleteAllAndInsertNewUseCase,
	val getPagedSearchObjectsUseCase: GetPagedSearchObjectsUseCase
)
