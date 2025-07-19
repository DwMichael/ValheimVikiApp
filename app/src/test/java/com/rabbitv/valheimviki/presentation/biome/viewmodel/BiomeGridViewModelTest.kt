package com.rabbitv.valheimviki.presentation.biome.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biomes_by_ids.GetBiomesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes.GetLocalBiomesUseCase
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
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class BiomeGridViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var biomeUseCases: BiomeUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getLocalBiomesUseCase: GetLocalBiomesUseCase

	@Mock
	private lateinit var getBiomeByIdUseCase: GetBiomeByIdUseCase

	@Mock
	private lateinit var getBiomesByIdsUseCase: GetBiomesByIdsUseCase


	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getLocalBiomesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))


		biomeUseCases = BiomeUseCases(
			getBiomeByIdUseCase = getBiomeByIdUseCase,
			getBiomesByIdsUseCase = getBiomesByIdsUseCase,
			getLocalBiomesUseCase = getLocalBiomesUseCase
		)
	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun biomeGridViewModel_uiState_initialLoading() = runTest {
		val viewModel = BiomeGridViewModel(biomeUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UIState.Loading)
		}
	}

	@Test
	fun biomeGridViewModel_uiState_emitsSuccessWhenDataIsAvailable() = runTest {
		val fakeBiomeList: List<Biome> = List(4) { index ->
			Biome(
				id = "1",
				category = "BIOME",
				subCategory = "",
				imageUrl = "",
				name = "",
				description = "",
				order = 1
			)
		}
		whenever(getLocalBiomesUseCase()).thenReturn(flowOf(fakeBiomeList))
		val viewModel = BiomeGridViewModel(biomeUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UIState.Loading)
			val result = awaitItem()

			when (result) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(fakeBiomeList, result.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun biomeGridViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val fakeBiomeList: List<Biome> = List(4) { index ->
			Biome(
				id = "1",
				category = "BIOME",
				subCategory = "",
				imageUrl = "",
				name = "",
				description = "",
				order = 1
			)
		}
		whenever(getLocalBiomesUseCase()).thenReturn(flowOf(fakeBiomeList))
		val viewModel = BiomeGridViewModel(biomeUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UIState.Loading)

			val result = awaitItem()

			when (result) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(fakeBiomeList, result.data)
				}
			}
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun biomeGridViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty() =
		runTest {
			whenever(getLocalBiomesUseCase()).thenReturn(flowOf(emptyList()))
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val viewModel = BiomeGridViewModel(biomeUseCases, connectivityObserver)

			viewModel.uiState.test {
				assert(awaitItem() is UIState.Loading)
				val state = awaitItem()


				when (state) {
					is UIState.Error -> {
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							state.message
						)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error")
					is UIState.Success -> fail("State should not emit Success")
				}
				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun biomeGridViewModel_uiState_remainsLoadingWhenConnectedAndListIsEmpty() = runTest {

		whenever(getLocalBiomesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		val viewModel = BiomeGridViewModel(biomeUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UIState.Loading)

			expectNoEvents()
		}
	}
}
