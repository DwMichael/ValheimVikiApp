package com.rabbitv.valheimviki.presentation.search.model

import com.rabbitv.valheimviki.domain.model.search.Search

data class SearchState(
	val results: List<Search> = emptyList(),
	val currentPage: Int = 0,
	val hasMorePages: Boolean = true,
	val query: String = "",
)