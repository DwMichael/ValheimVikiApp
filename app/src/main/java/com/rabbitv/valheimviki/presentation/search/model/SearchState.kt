package com.rabbitv.valheimviki.presentation.search.model

import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class SearchState(
	val query: String = "",
	val totalPages: Int = 1,
	val searchList: UIState<List<Search>> = UIState.Loading,
	val currentPage: Int = 1,
)