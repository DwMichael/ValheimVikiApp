package com.rabbitv.valheimviki.presentation.search.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert.DeleteAllAndInsertNewUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.get_paged_search_object.GetPagedSearchObjectsUseCase
import com.rabbitv.valheimviki.presentation.search.model.SearchUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class SearchViewModelTest {


	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var getPagedSearchObjectsUseCase: GetPagedSearchObjectsUseCase


	@Mock
	private lateinit var deleteAllAndInsertNewUseCase: DeleteAllAndInsertNewUseCase

	private lateinit var searchUseCases: SearchUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		searchUseCases = SearchUseCases(
			getPagedSearchObjectsUseCase = getPagedSearchObjectsUseCase,
			deleteAllAndInsertNewUseCase = deleteAllAndInsertNewUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun searchQuery_InitLoad_SearchQueryShouldBeBlank() = runTest {
		val viewModel = SearchViewModel(
			searchUseCases = searchUseCases
		)
		viewModel.searchQuery.test {
			val firstEmit = awaitItem()
			assertEquals("", firstEmit)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun onEvent_SearchQueryChanged_ShouldUpdateSearchQuery() = runTest {
		val viewModel = SearchViewModel(
			searchUseCases = searchUseCases
		)

		viewModel.searchQuery.test {
			val query = "Bread"
			val firstEmit = awaitItem()
			assertEquals("", firstEmit)
			viewModel.onEvent(SearchUiEvent.UpdateQuery(query))

			val secondEmit = awaitItem()
			assertEquals(query, secondEmit)
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun searchResults_flatMapLatest_callsUseCaseWithLatestQuery() = runTest {

		whenever(searchUseCases.getPagedSearchObjectsUseCase(any(), any()))
			.thenReturn(flowOf(PagingData.empty()))

		val viewModel = SearchViewModel(searchUseCases)


		val collectJob = launch {
			viewModel.searchResults.collect()
		}

		viewModel.onEvent(SearchUiEvent.UpdateQuery("Foo"))
		advanceTimeBy(600)

		viewModel.onEvent(SearchUiEvent.UpdateQuery("Bread"))
		advanceTimeBy(600)

		advanceUntilIdle()

		verify(searchUseCases.getPagedSearchObjectsUseCase)
			.invoke(eq("Foo"), eq(SearchViewModel.PAGE_SIZE))
		verify(searchUseCases.getPagedSearchObjectsUseCase)
			.invoke(eq("Bread"), eq(SearchViewModel.PAGE_SIZE))

		collectJob.cancel()
		viewModel.viewModelScope.cancel()
	}

}