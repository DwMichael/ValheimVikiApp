package com.rabbitv.valheimviki.presentation.armor.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id.GetArmorByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_ids.GetArmorsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case.GetLocalArmorsUseCase
import com.rabbitv.valheimviki.presentation.armor.model.ArmorUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
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
class ArmorListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var armorUseCases: ArmorUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getLocalArmorsUseCase: GetLocalArmorsUseCase

	@Mock
	private lateinit var getArmorByIdUseCase: GetArmorByIdUseCase

	@Mock
	private lateinit var getArmorsByIdsUseCase: GetArmorsByIdsUseCase
	

	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))


		armorUseCases = ArmorUseCases(
			getLocalArmorsUseCase = getLocalArmorsUseCase,
			getArmorByIdUseCase = getArmorByIdUseCase,
			getArmorsByIdsUseCase = getArmorsByIdsUseCase,
		)

	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


	@Test
	fun armorListViewModel_uiState_initialLoading() = runTest {

		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		viewModel.uiState.test {
			assert(awaitItem().armorsUiState is UIState.Loading)
		}
	}


	@Test
	fun armorListViewModel_chipSelectedChestArmor_isNull() = runTest {
		val fakeArmorList: List<Armor> = List(4) { index ->
			Armor(
				id = index.toString(),
				name = "Test Armor",
				subCategory = "Chest",
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = index,
			)
		}

		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(fakeArmorList))

		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		viewModel.uiState.test {
			val result = awaitItem()

			assertNull(result.selectedChip)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun armorListViewModel_uiState_emitsSuccessWhenDataIsAvailable() = runTest {
		val fakeArmorList: List<Armor> = List(4) { index ->
			Armor(
				id = index.toString(),
				name = "Test Armor",
				subCategory = "Chest",
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = index,
			)
		}
		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(fakeArmorList))

		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		viewModel.uiState.test {
			assert(awaitItem().armorsUiState is UIState.Loading)

			val result = awaitItem()

			when (result.armorsUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(result.selectedChip)
					assertEquals(fakeArmorList, result.armorsUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun armorListViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val fakeArmorList: List<Armor> = List(4) { index ->
			Armor(
				id = index.toString(),
				name = "Test Armor",
				subCategory = "Chest",
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = index,
			)
		}
		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(fakeArmorList))

		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		viewModel.uiState.test {
			assert(awaitItem().armorsUiState is UIState.Loading)

			val result = awaitItem()

			when (result.armorsUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertNull(result.selectedChip)
					assertEquals(fakeArmorList, result.armorsUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun armorListViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty() =
		runTest {
			whenever(getLocalArmorsUseCase()).thenReturn(flowOf(emptyList()))
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

			viewModel.uiState.test {
				assert(awaitItem().armorsUiState is UIState.Loading)

				val state = awaitItem()


				when (state.armorsUiState) {
					is UIState.Error -> {
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							state.armorsUiState.message
						)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error")
					is UIState.Success -> fail("State should not emit Success")
				}

				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun armorListViewModel_uiState_remainsLoadingWhenConnectedAndListIsEmpty() = runTest {

		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)


		viewModel.uiState.test {
			assert(awaitItem().armorsUiState is UIState.Loading)

			expectNoEvents()
		}
	}

	@Test
	fun armorListViewModel_armorsFlow_emitsFilteredArmorsWhenChipIsSelected() = runTest {

		val fakeArmorList: List<Armor> = listOf(
			Armor(
				id = "1",
				name = "Test Armor",
				subCategory = ArmorSubCategory.CHEST_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 1
			),
			Armor(
				id = "2",
				name = "Test Armor",
				subCategory = ArmorSubCategory.HELMET.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 2
			),
			Armor(
				id = "3",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 3
			),
			Armor(
				id = "4",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 4
			)
		)



		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flowOf(fakeArmorList))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		viewModel.armors.test {

			assertEquals(4, awaitItem().size)

			viewModel.onEvent(ArmorUiEvent.ChipSelected(ArmorSubCategory.LEG_ARMOR))

			val filteredList = awaitItem()
			assertEquals(2, filteredList.size)
			assertTrue(filteredList.all { it.subCategory == ArmorSubCategory.LEG_ARMOR.toString() })
		}

	}

	@Test
	fun armorListViewModel_armorsFlow_emitsAllArmorsWhenNoChipIsSelected() = runTest {

		val fakeArmorList: List<Armor> = listOf(
			Armor(
				id = "1",
				name = "Test Armor",
				subCategory = ArmorSubCategory.CHEST_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 1
			),
			Armor(
				id = "2",
				name = "Test Armor",
				subCategory = ArmorSubCategory.HELMET.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 2
			),
			Armor(
				id = "3",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 3
			),
			Armor(
				id = "4",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 4
			)
		)

		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flowOf(fakeArmorList))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)
		viewModel.armors.test {
			val armors = awaitItem()
			println(armors)
			assertEquals(4, armors.size)
			assertEquals(fakeArmorList, armors)
		}
	}


	@Test
	fun armorListViewModel_armorsFlow_handlesEmptyArmorList() = runTest {

		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flowOf(emptyList()))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)
		val result = viewModel.armors.first()
		assertTrue(result.isEmpty())
	}

	@Test
	fun armorListViewModel_armorsFlow_handlesChipSelectionChanges() = runTest {

		val fakeArmorList: List<Armor> = listOf(
			Armor(
				id = "1",
				name = "Test Armor",
				subCategory = ArmorSubCategory.CHEST_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 1
			),
			Armor(
				id = "2",
				name = "Test Armor",
				subCategory = ArmorSubCategory.HELMET.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 2
			),
			Armor(
				id = "3",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 3
			),
			Armor(
				id = "4",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 4
			)
		)

		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flowOf(fakeArmorList))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		val emissions = mutableListOf<List<Armor>>()
		val job = launch {
			viewModel.armors.take(3).collect { emissions.add(it) }
		}


		viewModel.onEvent(ArmorUiEvent.ChipSelected(ArmorSubCategory.LEG_ARMOR))
		advanceUntilIdle()

		viewModel.onEvent(ArmorUiEvent.ChipSelected(ArmorSubCategory.CHEST_ARMOR))
		advanceUntilIdle()

		viewModel.onEvent(ArmorUiEvent.ChipSelected(null))
		advanceUntilIdle()

		job.cancel()


		assertEquals(3, emissions.size)

		assertTrue(emissions[0].all { it.subCategory == ArmorSubCategory.LEG_ARMOR.toString() })

		assertTrue(emissions[1].all { it.subCategory == ArmorSubCategory.CHEST_ARMOR.toString() })

		assertEquals(fakeArmorList, emissions[2])
	}

	@Test
	fun armorListViewModel_armorsFlow_handlesUseCaseErrorsGracefully() = runTest {
		val exception = RuntimeException("Database error")
		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flow { throw exception })
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)
		val result = viewModel.armors.first()

		assertDoesNotThrow { result }
	}

	@Test
	fun armorListViewModel_armorsFlow_SwitchesTODefaultDispatcher() = runTest {
		val fakeArmorList: List<Armor> = listOf(
			Armor(
				id = "1",
				name = "Test Armor",
				subCategory = ArmorSubCategory.CHEST_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 1
			),
			Armor(
				id = "2",
				name = "Test Armor",
				subCategory = ArmorSubCategory.HELMET.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 2
			),
			Armor(
				id = "3",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 3
			),
			Armor(
				id = "4",
				name = "Test Armor",
				subCategory = ArmorSubCategory.LEG_ARMOR.toString(),
				imageUrl = "",
				category = "",
				description = "",
				upgradeInfoList = emptyList(),
				effects = "",
				usage = "",
				fullSetEffects = "",
				order = 4
			)
		)
		whenever(getLocalArmorsUseCase.invoke()).thenReturn(flowOf(fakeArmorList))
		val viewModel = ArmorListViewModel(armorUseCases, connectivityObserver, Dispatchers.IO)

		val result = viewModel.armors.first()


		assertEquals(fakeArmorList, result)

	}
}

