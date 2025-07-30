package com.rabbitv.valheimviki.presentation.mead.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_mead_by_id.GetMeadByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_ids.GetMeadsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase
import com.rabbitv.valheimviki.presentation.mead.model.MeadUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class MeadListViewModelTest {
	val meadBases = listOf(
		Mead(
			id = "10", // Fixed ID instead of Random.nextInt(0, 100).toString()
			imageUrl = "",
			category = AppCategory.MEAD.toString(),
			subCategory = MeadSubCategory.MEAD_BASE.toString(),
			name = "H Mead Test Object",
			description = "",
			recipeOutput = "",
			effect = "",
			duration = "",
			cooldown = "",
			order = 1 // Fixed order instead of Random.nextInt(0, 100)
		),
		Mead(
			id = "5", // Fixed ID
			imageUrl = "",
			category = AppCategory.MEAD.toString(),
			subCategory = MeadSubCategory.MEAD_BASE.toString(),
			name = "K Mead Test Object",
			description = "",
			recipeOutput = "",
			effect = "",
			duration = "",
			cooldown = "",
			order = 2 // Fixed order
		)
	)

	val potions = listOf(
		Mead(
			id = "3", // Fixed ID
			imageUrl = "",
			category = AppCategory.MEAD.toString(),
			subCategory = MeadSubCategory.POTION.toString(),
			name = "A Mead Test Object",
			description = "",
			recipeOutput = "",
			effect = "",
			duration = "",
			cooldown = "",
			order = 1 // Fixed order
		)
	)
	val meads = meadBases + potions
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var meadUseCases: MeadUseCases

	@Mock
	private lateinit var getLocalMeadsUseCase: GetLocalMeadsUseCase

	@Mock
	private lateinit var getMeadByIdUseCase: GetMeadByIdUseCase

	@Mock
	private lateinit var getMeadsByIdsUseCase: GetMeadsByIdsUseCase

	@Mock
	private lateinit var getMeadsBySubCategoryUseCase: GetMeadsBySubCategoryUseCase

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		meadUseCases = MeadUseCases(
			getLocalMeadsUseCase = getLocalMeadsUseCase,
			getMeadByIdUseCase = getMeadByIdUseCase,
			getMeadsByIdsUseCase = getMeadsByIdsUseCase,
			getMeadsBySubCategoryUseCase = getMeadsBySubCategoryUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


	@Test
	fun uiState_InitLoad_ShouldEmitLoadingWithSelectedCategory() = runTest {
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
		}
	}

	@Test
	fun uiState_MeadStateSuccess_ShouldEmitSuccessWithFilteredList() = runTest {
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(meads))
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
			val secondEmit = awaitItem()

			when (secondEmit.meadState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(meadBases.sortedBy { it.order }, secondEmit.meadState.data)
					assertEquals(2, secondEmit.meadState.data.size)
				}
			}
		}
	}

	@Test
	fun uiState_WithNoConnectionAndStoredData_ShouldEmitSuccessWithFilteredList() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))

		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(meads))
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
			val secondEmit = awaitItem()
			when (secondEmit.meadState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(meadBases.sortedBy { it.order }, secondEmit.meadState.data)
					assertEquals(2, secondEmit.meadState.data.size)
				}
			}

		}
	}


	@Test
	fun uiState_WithNoConnectionAndEmptyList_ShouldEmitErrorState() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(emptyList()))
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
			val secondEmit = awaitItem()
			when (secondEmit.meadState) {
				is UIState.Error -> {
					assertEquals(
						error_no_connection_with_empty_list_message.toString(),
						secondEmit.meadState.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit success")
			}
		}
	}


	@Test
	fun uiState_CategoryChanged_ShouldEmitChangedList() = runTest {
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(meads))
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
			awaitItem()
			viewModel.onEvent(MeadUiEvent.CategorySelected(MeadSubCategory.POTION))
			val thirdEmit = awaitItem()
			when (thirdEmit.meadState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(potions.sortedBy { it.order }, thirdEmit.meadState.data)
					assertEquals(1, thirdEmit.meadState.data.size)
					assertEquals(
						MeadSubCategory.POTION,
						thirdEmit.selectedCategory
					)
				}
			}
		}
	}

	@Test
	fun uiState_ConnectionAndEmptyList_ShouldEmitLoadingState() = runTest {

		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(emptyList()))

		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			val initEmit = awaitItem()
			assert(initEmit.meadState is UIState.Loading)
			assertEquals(MeadSubCategory.MEAD_BASE, initEmit.selectedCategory)
		}
	}

	@Test
	fun uiState_UpstreamError_ShouldEmitErrorState() = runTest {
		val errorMessage = "Critical database error"
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flow {
			throw RuntimeException(errorMessage)
		})

		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			val initialEmit = awaitItem()
			assert(initialEmit.meadState is UIState.Loading)

			val errorEmit = awaitItem()
			when (val state = errorEmit.meadState) {
				is UIState.Error -> {
					assertEquals(errorMessage, state.message)
				}

				else -> fail("The meadState should be UIState.Error, but was $state")
			}
		}
	}

}