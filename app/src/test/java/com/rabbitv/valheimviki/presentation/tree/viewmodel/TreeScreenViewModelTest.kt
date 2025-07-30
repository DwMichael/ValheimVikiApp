package com.rabbitv.valheimviki.presentation.tree.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.connection.NetworkConnectivityObserver
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees.GetLocalTreesUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id.GetTreeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.fail


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class TreeScreenViewModelTest {

	private val trees: List<Tree> = listOf(
		Tree(
			id = "10",
			imageUrl = "",
			category = AppCategory.TREE.toString(),
			subCategory = "",
			name = "H Tree Test Object",
			description = "",
			order = 2
		),
		Tree(
			id = "5",
			imageUrl = "",
			category = AppCategory.TREE.toString(),
			subCategory = "",
			name = "K Tree Test Object",
			description = "",
			order = 1
		)
	)
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivityObserver

	@Mock
	private lateinit var getLocalTreesUseCase: GetLocalTreesUseCase

	@Mock
	private lateinit var getTreeByIdUseCase: GetTreeByIdUseCase

	@Mock
	private lateinit var getTreesByIdsUseCase: GetTreesByIdsUseCase

	private lateinit var treeUseCases: TreeUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		treeUseCases = TreeUseCases(
			getLocalTreesUseCase = getLocalTreesUseCase,
			getTreeByIdUseCase = getTreeByIdUseCase,
			getTreesByIdsUseCase = getTreesByIdsUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun uiState_InitLoadState_ShouldEmitLoadingState() = runTest {
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flowOf(trees))

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
		}
	}

	@Test
	fun uiState_TreeDataIsAvailable_ShouldEmitSuccess() = runTest {
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flowOf(trees))

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
			val secondEmit = awaitItem()
			when (secondEmit) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(trees.sortedBy { it.order }, secondEmit.data)
					assertEquals(2, secondEmit.data.size)
				}
			}
		}
	}

	@Test
	fun uiState_TreeIsAvailableNoConnection_ShouldEmitSuccess() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flowOf(trees))

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
			val secondEmit = awaitItem()
			when (secondEmit) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(trees.sortedBy { it.order }, secondEmit.data)
					assertEquals(2, secondEmit.data.size)
				}
			}
		}
	}

	@Test
	fun uiState_ConnectionNoData_ShouldEmitLoading() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flowOf(trees))

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)

			expectNoEvents()
		}
	}

	@Test
	fun uiState_NoConnectionNoData_ShouldEmitError() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flowOf(emptyList()))

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
			val secondEmit = awaitItem()
			when (secondEmit) {
				is UIState.Error -> {
					assertEquals(
						error_no_connection_with_empty_list_message.toString(),
						secondEmit.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> fail("State should not emit success")
			}
		}
	}


	@Test
	fun uiState_IOExceptionError_ShouldEmitError() = runTest {

		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flow { throw IOException() })

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
			val secondEmit = awaitItem()
			when (secondEmit) {
				is UIState.Error -> {
					assertEquals(
						"Problem accessing local data.",
						secondEmit.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> fail("State should not emit success")
			}
		}
	}

	@Test
	fun uiState_UpStreamError_ShouldEmitError() = runTest {

		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(treeUseCases.getLocalTreesUseCase()).thenReturn(flow { throw RuntimeException() })

		val viewModel = TreeScreenViewModel(
			treesUseCases = treeUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
			val secondEmit = awaitItem()
			when (secondEmit) {
				is UIState.Error -> {
					assertEquals(
						"An unexpected error occurred.",
						secondEmit.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> fail("State should not emit success")
			}
		}
	}

}