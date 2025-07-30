package com.rabbitv.valheimviki.presentation.crafting

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_id.GetCraftingObjectByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_ids.GetCraftingObjectByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_sub_category_use_case.GetCraftingObjectsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_objects_by_ids.GetCraftingObjectsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_local_crafting_object_use_case.GetLocalCraftingObjectsUseCase
import com.rabbitv.valheimviki.presentation.crafting.viewmodel.CraftingListViewModel
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
class CraftingListScreenTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var craftingUseCases: CraftingObjectUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getLocalCraftingObjectsUseCase: GetLocalCraftingObjectsUseCase

	@Mock
	private lateinit var getCraftingObjectById: GetCraftingObjectByIdUseCase

	@Mock
	private lateinit var getCraftingObjectsByIds: GetCraftingObjectsByIdsUseCase

	@Mock
	private lateinit var getCraftingObjectByIds: GetCraftingObjectByIdsUseCase

	@Mock
	private lateinit var getCraftingObjectsBySubCategoryUseCase: GetCraftingObjectsBySubCategoryUseCase

	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getLocalCraftingObjectsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))


		craftingUseCases = CraftingObjectUseCases(
			getLocalCraftingObjectsUseCase = getLocalCraftingObjectsUseCase,
			getCraftingObjectById = getCraftingObjectById,
			getCraftingObjectsByIds = getCraftingObjectsByIds,
			getCraftingObjectByIds = getCraftingObjectByIds,
			getCraftingObjectsBySubCategoryUseCase = getCraftingObjectsBySubCategoryUseCase
		)

	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


	@Test
	fun craftingListViewModel_uiState_initialLoading() = runTest {

		val viewModel =
			CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)

		viewModel.uiState.test {
			assert(awaitItem().craftingListUiState is UIState.Loading)
		}
	}


	@Test
	fun craftingListViewModel_chipSelected_isNull() = runTest {
		val viewModel =
			CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)

		viewModel.uiState.test {
			val result = awaitItem()

			assertNull(result.selectedChip)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun craftingListViewModel_uiState_emitsSuccessWhenDataIsAvailable() = runTest {
		val fakeCraftingObjectList: List<CraftingObject> = List(4) { index ->
			CraftingObject(
				id = index.toString(),
				name = "Test Crafting Object",
				subCategory = "Chest",
				imageUrl = "",
				category = "",
				description = "",
				order = index,
			)
		}
		whenever(getLocalCraftingObjectsUseCase()).thenReturn(flowOf(fakeCraftingObjectList))

		val viewModel =
			CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)

		viewModel.uiState.test {
			assert(awaitItem().craftingListUiState is UIState.Loading)

			val result = awaitItem()

			when (result.craftingListUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(result.selectedChip)
					assertEquals(fakeCraftingObjectList, result.craftingListUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun craftingListViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val fakeCraftingObjectList: List<CraftingObject> = List(4) { index ->
			CraftingObject(
				id = index.toString(),
				name = "Test Crafting Object",
				subCategory = "Chest",
				imageUrl = "",
				category = "",
				description = "",
				order = index,
			)
		}
		whenever(getLocalCraftingObjectsUseCase()).thenReturn(flowOf(fakeCraftingObjectList))

		val viewModel =
			CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)

		viewModel.uiState.test {
			assert(awaitItem().craftingListUiState is UIState.Loading)

			val result = awaitItem()

			when (result.craftingListUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(result.selectedChip)
					assertEquals(fakeCraftingObjectList, result.craftingListUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun craftingListViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty() =
		runTest {
			whenever(getLocalCraftingObjectsUseCase()).thenReturn(flowOf(emptyList()))
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val viewModel =
				CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)

			viewModel.uiState.test {
				assert(awaitItem().craftingListUiState is UIState.Loading)

				val state = awaitItem()


				when (state.craftingListUiState) {
					is UIState.Error -> {
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							state.craftingListUiState.message
						)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error")
					is UIState.Success -> fail("State should not emit Success")
				}

				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun craftingListViewModel_uiState_remainsLoadingWhenConnectedAndListIsEmpty() = runTest {

		whenever(getLocalCraftingObjectsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		val viewModel =
			CraftingListViewModel(craftingUseCases, connectivityObserver, Dispatchers.Default)


		viewModel.uiState.test {
			assert(awaitItem().craftingListUiState is UIState.Loading)

			expectNoEvents()
		}
	}
}