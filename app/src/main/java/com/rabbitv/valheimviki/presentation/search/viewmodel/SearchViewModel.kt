package com.rabbitv.valheimviki.presentation.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.presentation.search.model.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchUseCases: SearchUseCases
) : ViewModel() {

	companion object {
		const val PAGE_SIZE = 30
	}

	private val _searchQuery = MutableStateFlow("")
	private val _currentPage = MutableStateFlow(1)

	private val _isLoading = MutableStateFlow(false)


	fun updateQuery(query: String) {
		_searchQuery.value = query
		_currentPage.value = 1
	}

	fun loadNextPage() {
		_currentPage.value++
	}

	fun loadPreviousPage() {
		if (_currentPage.value > 1) {
			_currentPage.value--
		}
	}

	fun loadSpecificPage(page: Int) {
		_currentPage.value = page
	}


	@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
	private val _totalOfPages: StateFlow<Int> = _searchQuery
		.debounce(300L)
		.mapLatest { query ->
			val totalResults = if (query.isBlank()) {
				searchUseCases.countSearchObjectsUseCase()
			} else {
				searchUseCases.countSearchObjectsByNameUseCase(query)
			}
			Log.d("SearchViewModel", "Query: $query, Total results: $totalResults")
			val totalPages = if (totalResults > 0) {
				ceil(totalResults.toDouble() / PAGE_SIZE).toInt()
			} else {
				0
			}
			totalPages
		}.flowOn(Dispatchers.IO)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = 0  // Zmienione z 1 na 0
		)

	@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
	private val _searchList: StateFlow<List<Search>> = combine(
		_searchQuery.debounce(300L),
		_currentPage
	) { query, page ->
		_isLoading.value = true
		val startFrom = (page - 1) * PAGE_SIZE
		val list = if (query.isBlank()) {
			searchUseCases.getAllSearchObjectsUseCase(PAGE_SIZE, startFrom).first()
		} else {
			searchUseCases.searchByNameUseCase(
				query = query,
				limit = PAGE_SIZE,
				offset = startFrom
			).first()
		}
		_isLoading.value = false
		list

	}.flowOn(Dispatchers.IO).stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = emptyList()
	)


	@OptIn(FlowPreview::class)
	val uiState = combine(
		_searchQuery,
		_totalOfPages,
		_searchList,
		_currentPage,
		_isLoading,
	) { query, totalPages, searchList, currentPage, isLoading ->
		SearchState(
			query = query,
			totalPages = totalPages,
			searchList = searchList,
			currentPage = currentPage,
			isLoading = isLoading,
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		SearchState(isLoading = true)
	)

}