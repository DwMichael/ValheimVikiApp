package com.rabbitv.valheimviki.presentation.search.viewmodel

import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert.DeleteAllAndInsertNewUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.get_paged_search_object.GetPagedSearchObjectsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class SearchViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivity: NetworkConnectivity

	@Mock
	private lateinit var getPagedSearchObjectsUseCase: GetPagedSearchObjectsUseCase


	@Mock
	private lateinit var deleteAllAndInsertNewUseCase: DeleteAllAndInsertNewUseCase

	private lateinit var searchUseCases: SearchUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivity.isConnected).thenReturn(flowOf(true))
		searchUseCases = SearchUseCases(
			getPagedSearchObjectsUseCase = getPagedSearchObjectsUseCase,
			deleteAllAndInsertNewUseCase = deleteAllAndInsertNewUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


}