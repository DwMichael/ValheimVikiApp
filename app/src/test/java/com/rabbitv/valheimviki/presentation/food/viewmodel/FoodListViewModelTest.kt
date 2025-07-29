package com.rabbitv.valheimviki.presentation.food.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_by_id.GetFoodByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase
import com.rabbitv.valheimviki.presentation.food.model.FoodListUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class FoodListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var foodUseCases: FoodUseCases

	@Mock
	private lateinit var getLocalFoodListUseCase: GetLocalFoodListUseCase

	@Mock
	private lateinit var getFoodListByIdsUseCase: GetFoodListByIdsUseCase

	@Mock
	private lateinit var getFoodByIdUseCase: GetFoodByIdUseCase

	@Mock
	private lateinit var getFoodBySubCategoryUseCase: GetFoodListBySubCategoryUseCase

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		foodUseCases = FoodUseCases(
			getFoodBySubCategoryUseCase = getFoodBySubCategoryUseCase,
			getFoodListByIdsUseCase = getFoodListByIdsUseCase,
			getFoodByIdUseCase = getFoodByIdUseCase,
			getLocalFoodListUseCase = getLocalFoodListUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun uiState_InitialLoad_EmitsLoadingState() = runTest {
		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)
		}
	}

	@Test
	fun selectedCategory_Initialization_IsCookedFood() = runTest {
		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			val result = awaitItem()
			assertNotNull(result.selectedCategory)
			assertEquals(FoodSubCategory.COOKED_FOOD, result.selectedCategory)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_DataAvailable_EmitsSuccess() = runTest {
		// Given
		val foodList: List<Food> = List(4) { index ->
			Food(
				id = index.toString(),
				name = "Test Food Object",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = index,
			)
		}
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				foodList
			)
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)

			val result = awaitItem()

			when (result.foodState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(FoodSubCategory.COOKED_FOOD, result.selectedCategory)
					assertEquals(foodList, result.foodState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_DataAvailableAndNoConnection_EmitsSuccess() = runTest {
		// Given
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val foodList: List<Food> = List(4) { index ->
			Food(
				id = index.toString(),
				name = "Test Food Object",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = index,
			)
		}
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				foodList
			)
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)

			val result = awaitItem()

			when (result.foodState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(FoodSubCategory.COOKED_FOOD, result.selectedCategory)
					assertEquals(foodList, result.foodState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_NoConnectionAndListEmpty_EmitsError() = runTest {
		// Given
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				emptyList()
			)
		)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)

			val state = awaitItem()

			when (state.foodState) {
				is UIState.Error -> {
					assertEquals(
						error_no_connection_with_empty_list_message.toString(),
						state.foodState.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit Success")
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_ConnectedAndListEmpty_RemainsLoading() = runTest {
		// Given
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)
			expectNoEvents()
		}
	}

	@Test
	fun foodsFlow_CategorySelected_EmitsFilteredFoodList() = runTest {
		// Given
		val cookedFoodList = listOf(
			Food(
				id = "1",
				name = "Cooked Food 1",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 1,
			),
			Food(
				id = "2",
				name = "Cooked Food 2",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 2,
			)
		)

		val uncookedFoodList = listOf(
			Food(
				id = "3",
				name = "Uncooked Food 1",
				subCategory = FoodSubCategory.UNCOOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 3,
			)
		)

		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				cookedFoodList
			)
		)
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.UNCOOKED_FOOD)).thenReturn(
			flowOf(
				uncookedFoodList
			)
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.foods.test {
			// Initial state should be COOKED_FOOD
			assertEquals(2, awaitItem().size)

			// Change to UNCOOKED_FOOD
			viewModel.onEvent(FoodListUiEvent.CategorySelected(FoodSubCategory.UNCOOKED_FOOD))

			val filteredList = awaitItem()
			assertEquals(1, filteredList.size)
			assertTrue(filteredList.all { it.subCategory == FoodSubCategory.UNCOOKED_FOOD.toString() })
		}
	}

	@Test
	fun foodsFlow_EmptyFoodList_HandlesCorrectly() = runTest {
		// Given
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				emptyList()
			)
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		val result = viewModel.foods.first()
		assertTrue(result.isEmpty())
	}

	@Test
	fun foodsFlow_CategorySelectionChanges_HandlesCorrectly() = runTest {
		// Given
		val cookedFoodList = listOf(
			Food(
				id = "1",
				name = "Cooked Food 1",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 1,
			),
			Food(
				id = "2",
				name = "Cooked Food 2",
				subCategory = FoodSubCategory.COOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 2,
			)
		)

		val uncookedFoodList = listOf(
			Food(
				id = "3",
				name = "Uncooked Food 1",
				subCategory = FoodSubCategory.UNCOOKED_FOOD.toString(),
				imageUrl = "",
				category = "",
				description = "",
				order = 3,
			)
		)

		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				cookedFoodList
			)
		)
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.UNCOOKED_FOOD)).thenReturn(
			flowOf(
				uncookedFoodList
			)
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.foods.test {
			// Initial state should be COOKED_FOOD
			val initialEmittedList = awaitItem()
			assertEquals(2, initialEmittedList.size)
			assertEquals(cookedFoodList, initialEmittedList)

			// Change to UNCOOKED_FOOD
			viewModel.onEvent(FoodListUiEvent.CategorySelected(FoodSubCategory.UNCOOKED_FOOD))
			val uncookedEmittedList = awaitItem()
			assertEquals(1, uncookedEmittedList.size)
			assertTrue(uncookedEmittedList.all { it.subCategory == FoodSubCategory.UNCOOKED_FOOD.toString() })

			// Change back to COOKED_FOOD
			viewModel.onEvent(FoodListUiEvent.CategorySelected(FoodSubCategory.COOKED_FOOD))
			val cookedFoodEmittedList = awaitItem()
			assertEquals(2, cookedFoodEmittedList.size)
			assertTrue(cookedFoodEmittedList.all { it.subCategory == FoodSubCategory.COOKED_FOOD.toString() })

			expectNoEvents()
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun foodsFlow_CatchesExceptions_EmitsEmptyList() = runTest {
		// Given
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flowOf(
				emptyList()
			)
		)
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.UNCOOKED_FOOD)).thenReturn(
			flow { throw RuntimeException("Database error") }
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.foods.test {
			// Initial state should be COOKED_FOOD (empty list)
			assertEquals(0, awaitItem().size)

			// Change to UNCOOKED_FOOD (should catch exception and emit empty list)
			viewModel.onEvent(FoodListUiEvent.CategorySelected(FoodSubCategory.UNCOOKED_FOOD))
			val result = awaitItem()
			assertTrue(result.isEmpty())
		}
	}

	@Test
	fun uiState_UnknownErrorAndExceptionMessageIsNull_HandlesCorrectly() = runTest {
		// Given
		whenever(getFoodBySubCategoryUseCase(FoodSubCategory.COOKED_FOOD)).thenReturn(
			flow {
				throw object : RuntimeException() {
					override val message: String? = null
				}
			}
		)

		// When
		val viewModel = FoodListViewModel(foodUseCases, connectivityObserver)

		// Then
		viewModel.uiState.test {
			assert(awaitItem().foodState is UIState.Loading)

			val state = awaitItem()

			when (state.foodState) {
				is UIState.Error -> {
					// The error message should not be null and should be a fallback message
					assertNotNull(state.foodState.message)
					assertTrue(state.foodState.message!!.isNotEmpty())
					assertEquals(FoodSubCategory.COOKED_FOOD, state.selectedCategory)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit Success")
			}

			cancelAndIgnoreRemainingEvents()
		}
	}
}