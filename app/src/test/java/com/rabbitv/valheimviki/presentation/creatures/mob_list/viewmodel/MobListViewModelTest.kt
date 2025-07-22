package com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
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
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
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
class MobListViewModelTest {
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
	fun mobListViewModel_uiState_initialLoading() = runTest {
		val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)

		viewModel.mobUiState.test {
			assertEquals(UIState.Loading, awaitItem().listState)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun mobListViewModel_initialSelectedSubCategory_isPassiveCreature() = runTest {
		val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)

		viewModel.mobUiState.test {
			val result = awaitItem()
			assertEquals(CreatureSubCategory.PASSIVE_CREATURE, result.selectedSubCategory)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun mobListViewModel_uiState_emitsSuccessWhenDataIsAvailable_isPassiveCreature() = runTest {
		val fakeCreatureList: List<Creature> = List(4) { index ->
			Creature(
				id = index.toString(),
				category = "PASSIVE",
				subCategory = "",
				imageUrl = "",
				name = "",
				description = "",
				order = index,
				levels = 3,
				baseHP = 500,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
				levelCreatureData = emptyList(),
				notes = "",
				abilities = "",
				biography = "",
				location = ""
			)
		}
		whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
			flowOf(
				fakeCreatureList
			)
		)

		val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)

		viewModel.mobUiState.test {
			// Initial Loading state
			assertEquals(UIState.Loading, awaitItem().listState)

			// Success state
			val result = awaitItem()

			when (val listState = result.listState) {
				is UIState.Error -> fail("State should not be Error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assertEquals(CreatureSubCategory.PASSIVE_CREATURE, result.selectedSubCategory)
					assertEquals(fakeCreatureList, listState.data)
				}
			}
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun mobListViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection_isPassiveCreature() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val fakeCreatureList: List<Creature> = List(4) { index ->
				Creature(
					id = index.toString(),
					category = "PASSIVE",
					subCategory = "",
					imageUrl = "",
					name = "",
					description = "",
					order = index,
					levels = 3,
					baseHP = 500,
					weakness = "",
					resistance = "",
					baseDamage = "",
					collapseImmune = "",
					forsakenPower = "",
					levelCreatureData = emptyList(),
					notes = "",
					abilities = "",
					biography = "",
					location = ""
				)
			}
			whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
				flowOf(fakeCreatureList)
			)

			val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)

			viewModel.mobUiState.test {
				// Initial Loading state
				assertEquals(UIState.Loading, awaitItem().listState)

				// Success state
				val result = awaitItem()
				when (val listState = result.listState) {
					is UIState.Error -> fail("State should not be Error")
					is UIState.Loading -> fail("State should have transitioned to Success")
					is UIState.Success -> {
						assertEquals(
							CreatureSubCategory.PASSIVE_CREATURE,
							result.selectedSubCategory
						)
						assertEquals(fakeCreatureList, listState.data)
					}
				}
				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun mobListViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty_isPassiveCreature() =
		runTest {
			whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
				flowOf(
					emptyList()
				)
			)
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val viewModel =
				MobListViewModel(creatureUseCases, connectivityObserver)

			viewModel.mobUiState.test {
				assert(awaitItem().listState is UIState.Loading)

				val state = awaitItem()


				when (state.listState) {
					is UIState.Error -> {
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							state.listState.message
						)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error")
					is UIState.Success -> fail("State should not emit Success")
				}

				cancelAndIgnoreRemainingEvents()
			}
		}


	@Test
	fun mobListViewModel_creaturesBySelectedSubCatFlow_handlesEmptyCreatureList() = runTest {

		whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
			flowOf(
				emptyList()
			)
		)
		val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)
		val result = viewModel.creaturesBySelectedSubCat.first()
		assertTrue(result.isEmpty())
	}

	@Test
	fun mobListViewModel_creatureFlow_handlesCategorySelectionChanges() = runTest {
		val passiveCreatures = listOf(
			Creature(
				id = "1",
				subCategory = CreatureSubCategory.PASSIVE_CREATURE.toString(),
				category = AppCategory.CREATURE.toString(),
				imageUrl = "",
				name = "",
				description = "",
				order = 1,
				levels = 3,
				baseHP = 500,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
				levelCreatureData = emptyList(),
				notes = "",
				abilities = "",
				biography = "",
				location = ""
			),
			Creature(
				id = "2",
				subCategory = CreatureSubCategory.PASSIVE_CREATURE.toString(),
				category = AppCategory.CREATURE.toString(),
				imageUrl = "",
				name = "",
				description = "",
				order = 2,
				levels = 3,
				baseHP = 500,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
				levelCreatureData = emptyList(),
				notes = "",
				abilities = "",
				biography = "",
				location = ""
			)
		)
		val aggressiveCreatures = listOf(
			Creature(
				id = "3",
				subCategory = CreatureSubCategory.AGGRESSIVE_CREATURE.toString(),
				category = AppCategory.CREATURE.toString(),
				imageUrl = "",
				name = "",
				description = "",
				order = 3,
				levels = 3,
				baseHP = 500,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
				levelCreatureData = emptyList(),
				notes = "",
				abilities = "",
				biography = "",
				location = ""
			)
		)
		val npcCreatures = listOf(
			Creature(
				id = "4",
				subCategory = CreatureSubCategory.NPC.toString(),
				category = AppCategory.CREATURE.toString(),
				imageUrl = "",
				name = "",
				description = "",
				order = 4,
				levels = 3,
				baseHP = 500,
				weakness = "",
				resistance = "",
				baseDamage = "",
				collapseImmune = "",
				forsakenPower = "",
				levelCreatureData = emptyList(),
				notes = "",
				abilities = "",
				biography = "",
				location = ""
			)
		)

		whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
			flowOf(
				passiveCreatures
			)
		)
		whenever(getCreaturesBySubCategory(CreatureSubCategory.AGGRESSIVE_CREATURE)).thenReturn(
			flowOf(aggressiveCreatures)
		)
		whenever(getCreaturesBySubCategory(CreatureSubCategory.NPC)).thenReturn(flowOf(npcCreatures))
		val viewModel = MobListViewModel(creatureUseCases, connectivityObserver)

		viewModel.creaturesBySelectedSubCat.test {

			val initialList = awaitItem()
			assertEquals(2, initialList.size)
			assertTrue(initialList.all { it.subCategory == CreatureSubCategory.PASSIVE_CREATURE.toString() })

			viewModel.onEvent(MobUiEvent.CategorySelected(CreatureSubCategory.AGGRESSIVE_CREATURE))
			val aggressiveList = awaitItem()
			assertEquals(1, aggressiveList.size)
			assertTrue(aggressiveList.all { it.subCategory == CreatureSubCategory.AGGRESSIVE_CREATURE.toString() })

			viewModel.onEvent(MobUiEvent.CategorySelected(CreatureSubCategory.NPC))
			val npcList = awaitItem()
			assertEquals(1, npcList.size)
			assertTrue(npcList.all { it.subCategory == CreatureSubCategory.NPC.toString() })

			viewModel.onEvent(MobUiEvent.CategorySelected(CreatureSubCategory.PASSIVE_CREATURE))
			val passiveListAgain = awaitItem()
			assertEquals(2, passiveListAgain.size)
			assertTrue(passiveListAgain.all { it.subCategory == CreatureSubCategory.PASSIVE_CREATURE.toString() })

			cancelAndIgnoreRemainingEvents()
		}
	}
}