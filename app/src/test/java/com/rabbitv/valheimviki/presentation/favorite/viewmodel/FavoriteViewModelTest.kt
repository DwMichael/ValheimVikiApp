package com.rabbitv.valheimviki.presentation.favorite.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.add_to_favorite.AddFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.delete_from_favorite.DeleteFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.get_all_favorite_items.GetAllFavoritesUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.is_favorite.IsFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.toggle_favorite.ToggleFavoriteUseCase
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiEvent
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.fail


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class FavoriteViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var favoriteUseCases: FavoriteUseCases

	@Mock
	private lateinit var isFavorite: IsFavoriteUseCase

	@Mock
	private lateinit var getAllFavoritesUseCase: GetAllFavoritesUseCase

	@Mock
	private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

	@Mock
	private lateinit var addFavoriteUseCase: AddFavoriteUseCase

	@Mock
	private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		favoriteUseCases = FavoriteUseCases(
			isFavorite = isFavorite,
			getAllFavoritesUseCase = getAllFavoritesUseCase,
			deleteFavoriteUseCase = deleteFavoriteUseCase,
			addFavoriteUseCase = addFavoriteUseCase,
			toggleFavoriteUseCase = toggleFavoriteUseCase,
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun favoriteViewModel_uiState_initialLoading() = runTest {
		val viewModel =
			FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().favoritesState is UIState.Loading)
		}
	}

	@Test
	fun favoriteViewModel_chipSelectedCategory_isNull() = runTest {
		val viewModel =
			FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			val result = awaitItem()

			assertNull(result.selectedCategory)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun favoriteViewModel_uiState_DataIsAvailableShouldReturnSuccess() = runTest {
		val favorites = listOf(
			Food(
				id = "cooked_meat",
				imageUrl = "https://picsum.photos/200/200?random=1",
				category = "FOOD",
				subCategory = "COOKED",
				name = "Cooked Meat Cooked Meat",
				description = "Perfectly grilled meat that restores health and stamina.",
				order = 1,
				eitr = null,
				health = 40,
				weight = 1.0,
				healing = 2,
				stamina = 20,
				duration = "15m",
				forkType = "Normal",
				stackSize = 20
			).toFavorite(),
			CraftingObject(
				id = "workbench",
				imageUrl = "https://picsum.photos/200/200?random=1",
				category = "CRAFTING",
				subCategory = "CRAFTING_STATION",
				name = "Workbench",
				description = "A basic crafting station for creating simple tools and weapons.",
				order = 1
			).toFavorite(),
			PointOfInterest(
				id = "poi_001",
				imageUrl = "https://example.com/images/tower_ruins.png",
				category = "Structure",
				subCategory = "Ruins",
				name = "Ancient Tower",
				description = "Crumbling remains of an ancient watchtower, now home to Greydwarfs.",
				order = 1
			).toFavorite(),

			)

		whenever(getAllFavoritesUseCase()).thenReturn(flowOf(favorites))
		val viewModel =
			FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().favoritesState is UIState.Loading)

			val uiState: FavoriteUiState = awaitItem()

			when (uiState.favoritesState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(uiState.selectedCategory)
					assertEquals(favorites.sortedBy { it.name }, uiState.favoritesState.data)
				}
			}
		}
	}

	@Test
	fun favoriteViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val favorites = listOf(
			Food(
				id = "cooked_meat",
				imageUrl = "https://picsum.photos/200/200?random=1",
				category = "FOOD",
				subCategory = "COOKED",
				name = "Cooked Meat Cooked Meat",
				description = "Perfectly grilled meat that restores health and stamina.",
				order = 1,
				eitr = null,
				health = 40,
				weight = 1.0,
				healing = 2,
				stamina = 20,
				duration = "15m",
				forkType = "Normal",
				stackSize = 20
			).toFavorite(),
			CraftingObject(
				id = "workbench",
				imageUrl = "https://picsum.photos/200/200?random=1",
				category = "CRAFTING",
				subCategory = "CRAFTING_STATION",
				name = "Workbench",
				description = "A basic crafting station for creating simple tools and weapons.",
				order = 1
			).toFavorite(),
			PointOfInterest(
				id = "poi_001",
				imageUrl = "https://example.com/images/tower_ruins.png",
				category = "Structure",
				subCategory = "Ruins",
				name = "Ancient Tower",
				description = "Crumbling remains of an ancient watchtower, now home to Greydwarfs.",
				order = 1
			).toFavorite(),

			)
		whenever(getAllFavoritesUseCase()).thenReturn(flowOf(favorites))
		val viewModel =
			FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)


		viewModel.uiState.test {
			assert(awaitItem().favoritesState is UIState.Loading)

			val result = awaitItem()

			when (result.favoritesState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(result.selectedCategory)
					assertEquals(favorites.sortedBy { it.name }, result.favoritesState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun favoriteViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty() =
		runTest {
			whenever(getAllFavoritesUseCase()).thenReturn(flowOf(emptyList()))
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))

			val viewModel =
				FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

			viewModel.uiState.test {
				assert(awaitItem().favoritesState is UIState.Loading)

				val state = awaitItem()


				when (state.favoritesState) {
					is UIState.Error -> {
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							state.favoritesState.message
						)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error")
					is UIState.Success -> fail("State should not emit Success")
				}

				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun favoriteViewModel_onEvent_updateSelectedCategory() = runTest {
		val viewModel =
			FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			val resultFirst = awaitItem()
			assertEquals(null, resultFirst.selectedCategory)
			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.CRAFTING))
			testDispatcher.scheduler.advanceUntilIdle()
			val result = awaitItem()
			assertEquals(AppCategory.CRAFTING, result.selectedCategory)
		}
	}

	@Test
	fun favoriteViewModel_onEvent_categorySelected_togglesWhenSameCategorySelected() = runTest {
		whenever(getAllFavoritesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		val viewModel = FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			val initialState = awaitItem()
			assertNull(initialState.selectedCategory)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.CRAFTING))
			testDispatcher.scheduler.advanceUntilIdle()
			val firstSelection = awaitItem()
			assertEquals(AppCategory.CRAFTING, firstSelection.selectedCategory)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.CRAFTING))
			testDispatcher.scheduler.advanceUntilIdle()
			val toggledState = awaitItem()
			assertNull(toggledState.selectedCategory)
		}
	}

	@Test
	fun favoriteViewModel_onEvent_categorySelected_switchesBetweenDifferentCategories() = runTest {
		whenever(getAllFavoritesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		val viewModel = FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			val initialState = awaitItem()
			assertNull(initialState.selectedCategory)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.FOOD))
			testDispatcher.scheduler.advanceUntilIdle()
			val foodSelection = awaitItem()
			assertEquals(AppCategory.FOOD, foodSelection.selectedCategory)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.CRAFTING))
			testDispatcher.scheduler.advanceUntilIdle()
			val craftingSelection = awaitItem()
			assertEquals(AppCategory.CRAFTING, craftingSelection.selectedCategory)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.ARMOR))
			testDispatcher.scheduler.advanceUntilIdle()
			val armorSelection = awaitItem()
			assertEquals(AppCategory.ARMOR, armorSelection.selectedCategory)
		}
	}


	@Test
	fun favoriteViewModel_uiState_preservesSelectedCategoryWhenErrorOccurs() = runTest {
		val favorites = listOf(
			Food(
				id = "cooked_meat",
				imageUrl = "https://picsum.photos/200/200?random=1",
				category = "FOOD",
				subCategory = "COOKED",
				name = "Cooked Meat",
				description = "Perfectly grilled meat that restores health and stamina.",
				order = 1,
				eitr = null,
				health = 40,
				weight = 1.0,
				healing = 2,
				stamina = 20,
				duration = "15m",
				forkType = "Normal",
				stackSize = 20
			).toFavorite()
		)

		whenever(getAllFavoritesUseCase()).thenReturn(flowOf(favorites))
		val viewModel = FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			skipItems(2)

			viewModel.onEvent(FavoriteUiEvent.CategorySelected(AppCategory.FOOD))
			testDispatcher.scheduler.advanceUntilIdle()
			val categorySelected = awaitItem()
			assertEquals(AppCategory.FOOD, categorySelected.selectedCategory)
			// Remove unnecessary stubbing after this point
		}
	}

	@Test
	fun favoriteViewModel_uiState_emitsErrorWhenExceptionOccursInFlow() = runTest {
		val exception = RuntimeException("Test exception")
		whenever(getAllFavoritesUseCase()).thenThrow(exception)

		val viewModel = FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().favoritesState is UIState.Loading)

			val errorState = awaitItem()
			when (errorState.favoritesState) {
				is UIState.Error -> {
					assertEquals("Test exception", errorState.favoritesState.message)
					assertNull(errorState.selectedCategory)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit Success when exception occurs")
			}
		}
	}

	@Test
	fun favoriteViewModel_uiState_handlesUnknownErrorWhenExceptionMessageIsNull() = runTest {
		val exception = RuntimeException(null as String?)
		whenever(getAllFavoritesUseCase()).thenThrow(exception)

		val viewModel = FavoriteViewModel(favoriteUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().favoritesState is UIState.Loading)

			val errorState = awaitItem()
			when (errorState.favoritesState) {
				is UIState.Error -> {
					assertEquals("An unknown error occurred", errorState.favoritesState.message)
					assertNull(errorState.selectedCategory)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit Success when exception occurs")
			}
		}
	}
}