package com.rabbitv.valheimviki.presentation.search.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import com.rabbitv.valheimviki.domain.use_cases.search.count_search_objects.CountSearchObjectsUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.count_search_objects_by_name.CountSearchObjectsByNameUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.delete_all_and_insert.DeleteAllAndInsertNewUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.get_all_search_objects.GetAllSearchObjectsUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.search_by_description.SearchByDescriptionUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.search_by_name.SearchByNameUseCase
import com.rabbitv.valheimviki.domain.use_cases.search.search_by_name_and_description.SearchByNameAndDescriptionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class SearchViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var countSearchObjectsByNameUseCase: CountSearchObjectsByNameUseCase

	@Mock
	private lateinit var countSearchObjectsUseCase: CountSearchObjectsUseCase

	@Mock
	private lateinit var getAllSearchObjectsUseCase: GetAllSearchObjectsUseCase

	@Mock
	private lateinit var searchByDescriptionUseCase: SearchByDescriptionUseCase

	@Mock
	private lateinit var searchByNameUseCase: SearchByNameUseCase

	@Mock
	private lateinit var searchByNameAndDescriptionUseCase: SearchByNameAndDescriptionUseCase

	@Mock
	private lateinit var deleteAllAndInsertNewUseCase: DeleteAllAndInsertNewUseCase

	private lateinit var searchUseCases: SearchUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		searchUseCases = SearchUseCases(
			countSearchObjectsByNameUseCase = countSearchObjectsByNameUseCase,
			countSearchObjectsUseCase = countSearchObjectsUseCase,
			getAllSearchObjectsUseCase = getAllSearchObjectsUseCase,
			searchByDescriptionUseCase = searchByDescriptionUseCase,
			searchByNameUseCase = searchByNameUseCase,
			searchByNameAndDescriptionUseCase = searchByNameAndDescriptionUseCase,
			deleteAllAndInsertNewUseCase = deleteAllAndInsertNewUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}
}