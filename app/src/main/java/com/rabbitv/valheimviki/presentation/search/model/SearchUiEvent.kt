package com.rabbitv.valheimviki.presentation.search.model

sealed class SearchUiEvent {
	data class UpdateQuery(val query: String) : SearchUiEvent()
}

