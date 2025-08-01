package com.rabbitv.valheimviki.presentation.search.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert.DeleteAllAndInsertNewUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.get_paged_search_object.GetPagedSearchObjectsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
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
	fun searchResults_InitLoad_SearchQueryShouldBeBlank() = runTest {
		val viewModel = SearchViewModel(
			searchUseCases = searchUseCases
		)
		viewModel.searchQuery.test {
			val firstEmit = awaitItem()
			assertEquals("", firstEmit)
			cancelAndIgnoreRemainingEvents()
		}
	}

}