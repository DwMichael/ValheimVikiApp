package com.rabbitv.valheimviki.presentation.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.presentation.search.model.SearchState
import com.rabbitv.valheimviki.presentation.search.model.SearchUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchUseCases: SearchUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	companion object {
		const val PAGE_SIZE = 30
	}

	private val _searchQuery = MutableStateFlow("")
	private val _currentPage = MutableStateFlow(1)

	fun onEvent(event: SearchUiEvent) {
		when (event) {
			is SearchUiEvent.UpdateQuery -> {
				_searchQuery.update { event.query }
				_currentPage.update { 1 }
			}

			SearchUiEvent.LoadNextPage -> _currentPage.update { current -> current + 1 }
			SearchUiEvent.LoadPreviousPage -> {
				_currentPage.update { current ->
					if (current > 1) {
						current - 1
					} else {
						current
					}
				}
			}

			is SearchUiEvent.LoadSpecificPage -> _currentPage.update { event.page }
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
	internal val totalOfPages: Flow<Int> = _searchQuery
		.debounce(300L)
		.mapLatest { query ->
			val totalResults = if (query.isBlank()) {
				searchUseCases.countSearchObjectsUseCase()
			} else {
				searchUseCases.countSearchObjectsByNameUseCase(query)
			}
			Log.d("SearchViewModel", "Query: $query, Total results: $totalResults")
			val totalPages: Int = if (totalResults > 0) {
				ceil(totalResults.toDouble() / PAGE_SIZE).toInt()
			} else {
				0
			}
			totalPages
		}.flowOn(defaultDispatcher)


	@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
	internal val searchList: Flow<UIState<List<Search>>> = _searchQuery.debounce(300L).combine(
		_currentPage
	) { query, page ->
		Pair(query, page)
	}.flatMapLatest { (query, page) ->
		flow {
			emit(UIState.Loading)
			try {
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
				emit(UIState.Success(list))
			} catch (e: Exception) {
				emit(UIState.Error(e.message ?: "Unknown error"))
			}
		}.flowOn(defaultDispatcher)
	}


	@OptIn(FlowPreview::class)
	val uiState = combine(
		_searchQuery,
		totalOfPages,
		searchList,
		_currentPage,
	) { query, totalPages, searchList, currentPage ->
		SearchState(
			query = query,
			totalPages = totalPages,
			searchList = searchList,
			currentPage = currentPage
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		SearchState(searchList = UIState.Loading)
	)

}