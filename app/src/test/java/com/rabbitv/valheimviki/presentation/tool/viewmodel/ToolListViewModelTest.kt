package com.rabbitv.valheimviki.presentation.tool.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case.GetLocalToolsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tool_by_id.GetToolByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_ids.GetToolsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case.GetToolsBySubCategoryUseCase
import com.rabbitv.valheimviki.presentation.tool.model.ToolListUiEvent
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
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class ToolListViewModelTest {
	private val toolsBuilding = listOf(
		ItemTool(
			id = "10",
			imageUrl = "",
			category = AppCategory.TOOL.toString(),
			subCategory = ToolSubCategory.BUILDING.toString(),
			name = "H Tool Test Object",
			description = "",
			order = 1
		),
		ItemTool(
			id = "5",
			imageUrl = "",
			category = AppCategory.TOOL.toString(),
			subCategory = ToolSubCategory.BUILDING.toString(),
			name = "A Tool Test Object",
			description = "",
			order = 2
		)
	)

	private val pickAxes = listOf(
		ItemTool(
			id = "3",
			imageUrl = "",
			category = AppCategory.TOOL.toString(),
			subCategory = ToolSubCategory.PICKAXES.toString(),
			name = "A Tool Test Object",
			description = "",
			order = 1
		)
	)
	private val tools = (toolsBuilding + pickAxes).sortedBy { it.order }
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivity: NetworkConnectivity

	@Mock
	private lateinit var getLocalToolsUseCase: GetLocalToolsUseCase

	@Mock
	private lateinit var getToolByIdUseCase: GetToolByIdUseCase

	@Mock
	private lateinit var getToolsByIdsUseCase: GetToolsByIdsUseCase

	@Mock
	private lateinit var getToolsBySubCategoryUseCase: GetToolsBySubCategoryUseCase

	private lateinit var toolUseCases: ToolUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		whenever(connectivity.isConnected).thenReturn(flowOf(true))
		toolUseCases = ToolUseCases(
			getLocalToolsUseCase = getLocalToolsUseCase,
			getToolByIdUseCase = getToolByIdUseCase,
			getToolsByIdsUseCase = getToolsByIdsUseCase,
			getToolsBySubCategoryUseCase = getToolsBySubCategoryUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
		val available = "das"
	}

	@Test
	fun uiState_InitLoad_SelectedChipNull() {

		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		assertNull(viewModel.uiState.value.selectedCategory)
	}

	@Test
	fun uiState_InitLoad_ShouldToolStateLoading() {

		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		assert(viewModel.uiState.value.toolState is UIState.Loading)
	}

	@Test
	fun uiState_DataAvailableWithConnection_ShouldEmitSuccess() = runTest {
		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(tools))
		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			skipItems(1)
			val secondEmit = awaitItem()

			when (secondEmit.toolState) {
				is UIState.Error -> fail("Should not Emit error when is data")
				is UIState.Loading -> fail("Should changed from Loading to Success state")
				is UIState.Success -> {
					assertNull(secondEmit.selectedCategory)
					assertEquals(tools, secondEmit.toolState.data)
				}

			}
		}
	}

	@Test
	fun uiState_DataAvailableNoConnection_ShouldEmitSuccess() = runTest {
		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(tools))
		whenever(connectivity.isConnected).thenReturn(flowOf(false))
		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			skipItems(1)
			val secondEmit = awaitItem()

			when (secondEmit.toolState) {
				is UIState.Error -> fail("Should not Emit error when is data")
				is UIState.Loading -> fail("Should changed from Loading to Success state")
				is UIState.Success -> {
					assertNull(secondEmit.selectedCategory)
					assertEquals(tools, secondEmit.toolState.data)
				}

			}
		}
	}

	@Test
	fun uiState_NoDataAvailableWithConnection_ShouldEmitLoading() = runTest {
		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivity.isConnected).thenReturn(flowOf(true))
		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			skipItems(1)
			assert(viewModel.uiState.value.toolState is UIState.Loading)
			expectNoEvents()
		}
	}

	@Test
	fun uiState_NoDataAvailableNoConnection_ShouldEmitSuccess() = runTest {
		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivity.isConnected).thenReturn(flowOf(false))
		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			skipItems(1)
			val secondEmit = awaitItem()

			when (secondEmit.toolState) {
				is UIState.Error -> {
					assertNull(secondEmit.selectedCategory)
					assertEquals(
						error_no_connection_with_empty_list_message.toString(),
						secondEmit.toolState.message
					)
				}

				is UIState.Loading -> fail("Should changed from Loading to Success state")
				is UIState.Success -> fail("Should not Emit success ")
			}
		}
	}


	@Test
	fun uiState_CatchException_ShouldEmitSuccess() = runTest {
		val error = "Error in upstream"
		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flow { throw RuntimeException(error) })
		whenever(connectivity.isConnected).thenReturn(flowOf(true))
		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		viewModel.uiState.test {
			skipItems(1)
			val secondEmit = awaitItem()

			when (secondEmit.toolState) {
				is UIState.Error -> {
					assertNull(secondEmit.selectedCategory)
					assertEquals(
						error,
						secondEmit.toolState.message
					)
				}

				is UIState.Loading -> fail("Should changed from Loading to Success state")
				is UIState.Success -> fail("Should not Emit success ")
			}
		}
	}


	@Test
	fun onEvent_UpdateCategory_ShouldEmitUpdatedValue() = runTest {

		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(tools))

		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)

		val firstEvent = ToolListUiEvent.CategorySelected(ToolSubCategory.BUILDING)
		val secondEvent = ToolListUiEvent.CategorySelected(ToolSubCategory.PICKAXES)

		viewModel.uiState.test {


			assertNull(awaitItem().selectedCategory)

			viewModel.onEvent(firstEvent)
			val stateAfterFirstEvent = awaitItem()
			assertEquals(ToolSubCategory.BUILDING, stateAfterFirstEvent.selectedCategory)

			val successState1 = stateAfterFirstEvent.toolState as UIState.Success
			assertEquals(toolsBuilding, successState1.data)


			viewModel.onEvent(secondEvent)
			val stateAfterSecondEvent = awaitItem()
			assertEquals(ToolSubCategory.PICKAXES, stateAfterSecondEvent.selectedCategory)

			val successState2 = stateAfterSecondEvent.toolState as UIState.Success
			assertEquals(pickAxes, successState2.data)
		}
	}

	@Test
	fun onEvent_TheSameCategorySelected_ShouldChangedToNull() = runTest {

		whenever(toolUseCases.getLocalToolsUseCase()).thenReturn(flowOf(tools))

		val viewModel = ToolListViewModel(
			toolUseCases = toolUseCases,
			connectivityObserver = connectivity,
			defaultDispatcher = testDispatcher
		)
		viewModel.uiState.test {

			awaitItem()

			viewModel.onEvent(ToolListUiEvent.CategorySelected(ToolSubCategory.BUILDING))
			val secondEmit = awaitItem()
			assertEquals(ToolSubCategory.BUILDING, secondEmit.selectedCategory)

			viewModel.onEvent(ToolListUiEvent.CategorySelected(ToolSubCategory.BUILDING))
			val thirdEmit = awaitItem()
			assertNull(thirdEmit.selectedCategory)
		}
	}
}