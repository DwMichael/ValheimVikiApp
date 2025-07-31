package com.rabbitv.valheimviki.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.presentation.search.model.SearchUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchUseCases: SearchUseCases
) : ViewModel() {
	companion object {
		const val PAGE_SIZE = 30
	}

	private val _searchQuery = MutableStateFlow("")
	val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

	@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
	val searchResults: Flow<PagingData<Search>> = _searchQuery
		.debounce(300L)
		.distinctUntilChanged()
		.flatMapLatest { query ->
			searchUseCases.getPagedSearchObjectsUseCase(query, PAGE_SIZE)
		}
		.cachedIn(viewModelScope)

	fun onEvent(event: SearchUiEvent) {
		if (event is SearchUiEvent.UpdateQuery) {
			_searchQuery.value = event.query
		}
	}
}