package com.rabbitv.valheimviki.presentation.search.model

sealed class SearchUiEvent {
	data class UpdateQuery(val query: String) : SearchUiEvent()
	object LoadNextPage : SearchUiEvent()
	object LoadPreviousPage : SearchUiEvent()
	data class LoadSpecificPage(val page: Int) : SearchUiEvent()
}

