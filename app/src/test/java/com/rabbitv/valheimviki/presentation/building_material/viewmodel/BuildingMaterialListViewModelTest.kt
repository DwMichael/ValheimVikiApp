package com.rabbitv.valheimviki.presentation.building_material.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_material_by_id.GetBuildMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_ids.GetBuildMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory.GetBuildMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory_and_subtype.GetBuildMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_local_building_materials.GetLocalBuildMaterialsUseCase
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class BuildingMaterialListViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var buildingMaterialUseCases: BuildMaterialUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getLocalBuildMaterial: GetLocalBuildMaterialsUseCase

	@Mock
	private lateinit var getBuildMaterialByIds: GetBuildMaterialsByIdsUseCase

	@Mock
	private lateinit var getBuildMaterialById: GetBuildMaterialByIdUseCase

	@Mock
	private lateinit var getBuildMaterialsBySubCategory: GetBuildMaterialsBySubCategoryUseCase

	@Mock
	private lateinit var getBuildMaterialsBySubCategoryAndSubType: GetBuildMaterialsBySubCategoryAndSubTypeUseCase


	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		buildingMaterialUseCases = BuildMaterialUseCases(
			getLocalBuildMaterial = getLocalBuildMaterial,
			getBuildMaterialByIds = getBuildMaterialByIds,
			getBuildMaterialById = getBuildMaterialById,
			getBuildMaterialsBySubCategory = getBuildMaterialsBySubCategory,
			getBuildMaterialsBySubCategoryAndSubType = getBuildMaterialsBySubCategoryAndSubType,
		)
	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


	@Test
	fun buildingMaterialListViewModel_uiState_initialLoading() = runTest {
		val viewModel = BuildingMaterialListViewModel(
			buildingMaterialUseCases,
			connectivityObserver,
			Dispatchers.Default
		)

		viewModel.uiState.test {
			assertEquals(UIState.Loading, awaitItem().listState)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun buildingMaterialListViewModel_initialSelectedSubCategory_isPassiveCreature() = runTest {
		val viewModel = BuildingMaterialListViewModel(
			buildingMaterialUseCases,
			connectivityObserver,
			Dispatchers.Default
		)

		viewModel.uiState.test {
			val result = awaitItem()
			assertEquals(CreatureSubCategory.PASSIVE_CREATURE, result.selectedSubCategory)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun buildingMaterialListViewModel_uiState_emitsSuccessWhenDataIsAvailable_isPassiveCreature() =
		runTest {
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

			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases,
				connectivityObserver,
				Dispatchers.Default
			)

			viewModel.uiState.test {
				// Initial Loading state
				assertEquals(UIState.Loading, awaitItem().listState)

				// Success state
				val result = awaitItem()

				when (val listState = result.listState) {
					is UIState.Error -> fail("State should not be Error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
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
	fun buildingMaterialListViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection_isPassiveCreature() =
		runTest {
			whenever(
				connectivityObserver,
				Dispatchers.Default.isConnected
			).thenReturn(flowOf(false))
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

			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases,
				connectivityObserver,
				Dispatchers.Default
			)

			viewModel.uiState.test {
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
	fun buildingMaterialListViewModel_uiState_emitsErrorWhenConnectionIsNotAvailableAndListIsEmpty_isPassiveCreature() =
		runTest {
			whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
				flowOf(
					emptyList()
				)
			)
			whenever(
				connectivityObserver,
				Dispatchers.Default.isConnected
			).thenReturn(flowOf(false))
			val viewModel =
				BuildingMaterialListViewModel(
					buildingMaterialUseCases,
					connectivityObserver,
					Dispatchers.Default
				)

			viewModel.uiState.test {
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
	fun buildingMaterialListViewModel_creaturesBySelectedSubCatFlow_handlesEmptyCreatureList() =
		runTest {

			whenever(getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE)).thenReturn(
				flowOf(
					emptyList()
				)
			)
			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases,
				connectivityObserver,
				Dispatchers.Default
			)
			val result = viewModel.creaturesBySelectedSubCat.first()
			assertTrue(result.isEmpty())
		}

	@Test
	fun buildingMaterialListViewModel_armorsFlow_handlesCategorySelectionChanges() = runTest {
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
		val viewModel = BuildingMaterialListViewModel(
			buildingMaterialUseCases,
			connectivityObserver,
			Dispatchers.Default
		)

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