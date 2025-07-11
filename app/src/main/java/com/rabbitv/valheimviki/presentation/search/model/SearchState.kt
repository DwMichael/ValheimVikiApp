package com.rabbitv.valheimviki.presentation.search.model

import com.rabbitv.valheimviki.domain.model.search.Search

data class SearchState(
	val query: String = "",
	val totalPages: Int = 1,
	val searchList: List<Search> = emptyList(),
	val currentPage: Int = 1,
	val isLoading: Boolean = false,
)