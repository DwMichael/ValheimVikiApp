package com.rabbitv.valheimviki.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.presentation.search.model.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchUseCases: SearchUseCases
) : ViewModel() {

	companion object {
		const val PAGE_SIZE = 30
	}

	private val _searchQuery = MutableStateFlow("")
	private val _currentPage = MutableStateFlow(0)


	@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
	private val _searchResultsFlow: Flow<UiState<SearchState>> = combine(
		_searchQuery.debounce(300).distinctUntilChanged(),
		_currentPage
	) { query, page ->
		Pair(query, page)
	}.flatMapLatest { (query, page) ->
		flow {
			emit(UiState.Loading())

			try {
				val fetchedList: List<Search> = if (query.isBlank()) {
					searchUseCases.getAllSearchObjectsUseCase(PAGE_SIZE, page).first()
				} else {
					searchUseCases.searchByNameUseCase(query, PAGE_SIZE, page).first()
				}

				val hasMorePages = fetchedList.size == PAGE_SIZE

				emit(
					UiState.Success(
						SearchState(
							results = fetchedList,
							currentPage = page,
							hasMorePages = hasMorePages,
							query = query
						)
					)
				)
			} catch (e: Exception) {
				emit(UiState.Error("Failed to load search results: ${e.message}"))
			}
		}.flowOn(Dispatchers.IO)
	}


	val uiState: StateFlow<UiState<SearchState>> = _searchResultsFlow.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = UiState.Loading()
	)

	fun updateSearchQuery(query: String) {
		_searchQuery.value = query
		_currentPage.value = 0
	}

	fun loadNextPage() {
		val currentUiState = uiState.value
		if (currentUiState is UiState.Success) {
			if (currentUiState.data.hasMorePages) {
				_currentPage.value = currentUiState.data.currentPage + 1
			}
		}
	}

	fun loadPreviousPage() {
		val currentUiState = uiState.value
		if (currentUiState is UiState.Success) {
			if (currentUiState.data.currentPage > 0) {
				_currentPage.value = currentUiState.data.currentPage - 1
			}
		}
	}

	fun loadSpecificPage(pageNumber: Int) {
		if (pageNumber >= 0) {
			_currentPage.value = pageNumber
		}
	}
}