package com.rabbitv.valheimviki.presentation.creatures.bosses.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category.GetCreatureByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_subcategory.GetCreatureBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_relation_and_sub_category.GetCreaturesByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures.GetLocalCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_passive_creatures.GetPassiveCreature
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
class BossesGridViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var creatureUseCases: CreatureUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getCreaturesByIds: GetCreaturesByIdsUseCase

	@Mock
	private lateinit var getCreatureById: GetCreatureByIdUseCase

	@Mock
	private lateinit var getCreaturesBySubCategory: GetCreatureBySubCategoryUseCase

	@Mock
	private lateinit var getCreatureByIdAndSubCategoryUseCase: GetCreatureByIdAndSubCategoryUseCase

	@Mock
	private lateinit var getCreatureByRelationAndSubCategory: GetCreatureByRelationAndSubCategory

	@Mock
	private lateinit var getCreaturesByRelationAndSubCategory: GetCreaturesByRelationAndSubCategory

	@Mock
	private lateinit var getMainBossesUseCase: GetMainBossesUseCase

	@Mock
	private lateinit var getMiniBossesUseCase: GetMiniBossesUseCase

	@Mock
	private lateinit var getAggressiveCreatures: GetAggressiveCreatures

	@Mock
	private lateinit var getPassiveCreature: GetPassiveCreature

	@Mock
	private lateinit var getNPCsUseCase: GetNPCsUseCase

	@Mock
	private lateinit var getLocalCreaturesUseCase: GetLocalCreaturesUseCase


	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getMainBossesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))


		creatureUseCases = CreatureUseCases(
			getCreaturesByIds = getCreaturesByIds,
			getCreatureById = getCreatureById,
			getCreaturesBySubCategory = getCreaturesBySubCategory,
			getCreatureByIdAndSubCategoryUseCase = getCreatureByIdAndSubCategoryUseCase,
			getCreatureByRelationAndSubCategory = getCreatureByRelationAndSubCategory,
			getCreaturesByRelationAndSubCategory = getCreaturesByRelationAndSubCategory,
			getMainBossesUseCase = getMainBossesUseCase,
			getMiniBossesUseCase = getMiniBossesUseCase,
			getAggressiveCreatures = getAggressiveCreatures,
			getPassiveCreature = getPassiveCreature,
			getNPCsUseCase = getNPCsUseCase,
			getLocalCreaturesUseCase = getLocalCreaturesUseCase
		)
	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun bossGridViewModel_uiState_initialLoading() = runTest {
		val viewModel = BossesGridViewModel(creatureUseCases, connectivityObserver)

		viewModel.mainBossUiListState.test {
			assert(awaitItem() is UIState.Loading)
		}
	}

	@Test
	fun bossGridViewModel_uiState_emitsSuccessWhenDataIsAvailable() = runTest {
		val fakeBossList: List<MainBoss> = List(4) { index ->
			MainBoss(
				id = "1",
				category = "BIOME",
				subCategory = "",
				imageUrl = "",
				name = "",
				description = "",
				order = 1,
				baseHP = 50,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
			)
		}
		whenever(getMainBossesUseCase()).thenReturn(flowOf(fakeBossList))
		val viewModel = BossesGridViewModel(creatureUseCases, connectivityObserver)

		viewModel.mainBossUiListState.test {
			assert(awaitItem() is UIState.Loading)
			val result = awaitItem()

			when (result) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(fakeBossList, result.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun bossGridViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
		val fakeBossList: List<MainBoss> = List(4) { index ->
			MainBoss(
				id = "1",
				category = "BIOME",
				subCategory = "",
				imageUrl = "",
				name = "",
				description = "",
				order = 1,
				baseHP = 50,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
			)
		}
		whenever(getMainBossesUseCase()).thenReturn(flowOf(fakeBossList))
		val viewModel = BossesGridViewModel(creatureUseCases, connectivityObserver)

		viewModel.mainBossUiListState.test {
			assert(awaitItem() is UIState.Loading)

			val result = awaitItem()

			when (result) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(fakeBossList, result.data)
				}
			}
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun bossGridViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty() =
		runTest {
			whenever(getMainBossesUseCase()).thenReturn(flowOf(emptyList()))
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val viewModel = BossesGridViewModel(creatureUseCases, connectivityObserver)

			viewModel.mainBossUiListState.test {
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
	fun bossGridViewModel_uiState_remainsLoadingWhenConnectedAndListIsEmpty() = runTest {

		whenever(getMainBossesUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		val viewModel = BossesGridViewModel(creatureUseCases, connectivityObserver)

		viewModel.mainBossUiListState.test {
			assert(awaitItem() is UIState.Loading)

			expectNoEvents()
		}
	}
}
