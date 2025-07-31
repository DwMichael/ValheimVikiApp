package com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
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
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class OreDepositScreenViewModelTest {
	private val oreDeposits: List<OreDeposit> = listOf(
		OreDeposit(
			id = "10",
			imageUrl = "",
			category = AppCategory.OREDEPOSITE.toString(),
			subCategory = "",
			name = "H Ore Deposit Test Object",
			description = "",
			order = 2
		),
		OreDeposit(
			id = "5",
			imageUrl = "",
			category = AppCategory.OREDEPOSITE.toString(),
			subCategory = "",
			name = "K Ore Deposit Test Object",
			description = "",
			order = 1
		)
	)

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var oreDepositUseCases: OreDepositUseCases

	@Mock
	private lateinit var getLocalOreDepositsUseCase: GetLocalOreDepositUseCase

	@Mock
	private lateinit var getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase

	@Mock
	private lateinit var getOreDepositByIdUseCase: GetOreDepositByIdUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		oreDepositUseCases = OreDepositUseCases(
			getLocalOreDepositsUseCase = getLocalOreDepositsUseCase,
			getOreDepositsByIdsUseCase = getOreDepositsByIdsUseCase,
			getOreDepositByIdUseCase = getOreDepositByIdUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun uiState_InitLoadState_ShouldEmitLoadingState() = runTest {
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flowOf(oreDeposits))

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
			connectivityObserver = connectivityObserver
		)

		viewModel.uiState.test {
			val firstEmit = awaitItem()
			assert(firstEmit is UIState.Loading)
		}
	}

	@Test
	fun uiState_OreDataIsAvailable_ShouldEmitSuccess() = runTest {
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flowOf(oreDeposits))

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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
					assertEquals(oreDeposits.sortedBy { it.order }, secondEmit.data)
					assertEquals(2, secondEmit.data.size)
				}
			}
		}
	}

	@Test
	fun uiState_OreDataIsAvailableNoConnection_ShouldEmitSuccess() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flowOf(oreDeposits))

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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
					assertEquals(oreDeposits.sortedBy { it.order }, secondEmit.data)
					assertEquals(2, secondEmit.data.size)
				}
			}
		}
	}

	@Test
	fun uiState_ConnectionNoData_ShouldEmitLoading() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flowOf(emptyList()))

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flowOf(emptyList()))

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flow { throw IOException() })

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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
		whenever(oreDepositUseCases.getLocalOreDepositsUseCase()).thenReturn(flow { throw RuntimeException() })

		val viewModel = OreDepositScreenViewModel(
			oreDepositUseCases = oreDepositUseCases,
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