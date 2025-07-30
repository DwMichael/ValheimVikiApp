package com.rabbitv.valheimviki.presentation.material.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials.GetLocalMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id.GetMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory.GetMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype.GetMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.presentation.material.model.MaterialUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class MaterialListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var materialUseCases: MaterialUseCases

	@Mock
	private lateinit var getLocalMaterials: GetLocalMaterialsUseCase

	@Mock
	private lateinit var getMaterialsByIds: GetMaterialsByIdsUseCase

	@Mock
	private lateinit var getMaterialById: GetMaterialByIdUseCase

	@Mock
	private lateinit var getMaterialsBySubCategory: GetMaterialsBySubCategoryUseCase

	@Mock
	private lateinit var getMaterialsBySubCategoryAndSubType: GetMaterialsBySubCategoryAndSubTypeUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		materialUseCases = MaterialUseCases(
			getLocalMaterials = getLocalMaterials,
			getMaterialsByIds = getMaterialsByIds,
			getMaterialById = getMaterialById,
			getMaterialsBySubCategory = getMaterialsBySubCategory,
			getMaterialsBySubCategoryAndSubType = getMaterialsBySubCategoryAndSubType
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun uiState_MaterialsUiStateInitLoad_EmitsLoadingState() = runTest {

		val viewModel = MaterialListViewModel(
			materialUseCases = materialUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)


		viewModel.uiState.test {
			assert(awaitItem().materialsUiState is UIState.Loading)

		}
	}

	@Test
	fun uiState_MaterialsUiStateSuccessWithConnection_ShouldEmitSuccessWithMaterialList() =
		runTest {
			val creatureDrop = List(4) { index ->
				Material(
					id = index.toString(),
					name = "Material Object Test",
					subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = Random.nextInt(0, 100),
				)
			}
			val miscList = listOf(
				Material(
					id = "6",
					name = "Material Object Test",
					category = AppCategory.MATERIAL.toString(),
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					description = "",
					order = 6,
				),
				Material(
					id = "5",
					name = "Material Object Test",
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = 5,
				),

				)
			val materials = creatureDrop + miscList
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(materials))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)


			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
				val result = awaitItem()

				when (result.materialsUiState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(MaterialSubCategory.CREATURE_DROP, result.selectedCategory)
						assertNull(result.selectedChip)
						assertEquals(
							creatureDrop.sortedBy { it.order },
							result.materialsUiState.data
						)
						assertEquals(4, result.materialsUiState.data.size)
					}
				}

			}
		}


	@Test
	fun uiState_MaterialsUiStateSuccessWithOutConnection_ShouldEmitSuccessWithMaterialList() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			val creatureDrop = List(4) { index ->
				Material(
					id = index.toString(),
					name = "Material Object Test",
					subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = Random.nextInt(0, 100),
				)
			}
			val miscList = listOf(
				Material(
					id = "6",
					name = "Material Object Test",
					category = AppCategory.MATERIAL.toString(),
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					description = "",
					order = 6,
				),
				Material(
					id = "5",
					name = "Material Object Test",
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = 5,
				),

				)
			val materials = creatureDrop + miscList
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(materials))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)


			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
				val result = awaitItem()

				when (result.materialsUiState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(MaterialSubCategory.CREATURE_DROP, result.selectedCategory)
						assertNull(result.selectedChip)
						assertEquals(
							creatureDrop.sortedBy { it.order },
							result.materialsUiState.data
						)
						assertEquals(4, result.materialsUiState.data.size)

					}
				}
			}
		}


	@Test
	fun uiState_MaterialsUiStateEmptyWithConnection_RemainsLoadingState() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(emptyList()))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
				val result = awaitItem()

				when (result.materialsUiState) {
					is UIState.Loading -> {
						assertEquals(MaterialSubCategory.CREATURE_DROP, result.selectedCategory)
						assertNull(result.selectedChip)
					}

					is UIState.Success -> fail("Should not be success with empty materials")
					is UIState.Error -> fail("Should not be error when connected")
				}
			}
		}

	@Test
	fun uiState_MaterialsUiStateEmptyWithOutConnection_EmitErrorStateWithNoConnectionMessage() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(emptyList()))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				val errorStateDueToNoConnection = awaitItem()

				when (errorStateDueToNoConnection.materialsUiState) {
					is UIState.Error -> {
						assertNull(errorStateDueToNoConnection.selectedCategory)
						assertNull(errorStateDueToNoConnection.selectedChip)
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							errorStateDueToNoConnection.materialsUiState.message
						)
					}

					else -> fail("Initial state should transition to Error due to no connection. Actual: ${errorStateDueToNoConnection.materialsUiState}")
				}
			}
		}

	@Test
	fun uiState_MaterialsUiStateThrowError_EmitErrorState() =
		runTest {
			val errorMessage = "Failed to load materials from local source!"
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flow {
				throw RuntimeException(
					errorMessage
				)
			})

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				val result = awaitItem()

				when (result.materialsUiState) {
					is UIState.Error -> {
						assertEquals(errorMessage, result.materialsUiState.message)
						assertNull(result.selectedCategory)
						assertNull(result.selectedChip)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error State")
					is UIState.Success -> fail("State should emit error state")
				}
			}
		}


	@Test
	fun uiState_MaterialsUiStateCategoryChanged_ShouldChangedCategory() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
			val creatureDrop = List(4) { index ->
				Material(
					id = index.toString(),
					name = "Material Object Test",
					subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = Random.nextInt(0, 100),
				)
			}
			val miscList = listOf(
				Material(
					id = "6",
					name = "Material Object Test",
					category = AppCategory.MATERIAL.toString(),
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					description = "",
					order = 6,
				),
				Material(
					id = "5",
					name = "Material Object Test",
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = 5,
				),

				)
			val materials = creatureDrop + miscList
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(materials))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = UnconfinedTestDispatcher()
			)


			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
				val creatureDropResult = awaitItem()

				assert(creatureDropResult.materialsUiState is UIState.Success)
				assertNull(creatureDropResult.selectedChip)
				assertEquals(MaterialSubCategory.CREATURE_DROP, creatureDropResult.selectedCategory)
				assertEquals(4, (creatureDropResult.materialsUiState as UIState.Success).data.size)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.MISCELLANEOUS))
				val miscResults = awaitItem()

				when (miscResults.materialsUiState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(
							MaterialSubCategory.MISCELLANEOUS,
							miscResults.selectedCategory
						)
						assertNull(miscResults.selectedChip)

						val expectedMiscData = miscList.sortedBy { it.order }
						assertEquals(expectedMiscData, miscResults.materialsUiState.data)
						assertEquals(2, miscResults.materialsUiState.data.size)
					}
				}
			}
		}

	@Test
	fun uiState_MaterialsUiStateChipTypeChanged_ShouldChangedCategory() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
			val creatureDrop = List(4) { index ->
				Material(
					id = index.toString(),
					name = "Material Object Test",
					subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
					subType = MaterialSubType.LOOT.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = Random.nextInt(0, 100),
				)
			}
			val miscList = listOf(
				Material(
					id = "6",
					name = "Material Object Test",
					category = AppCategory.MATERIAL.toString(),
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					subType = MaterialSubType.ITEM.toString(),
					imageUrl = "",
					description = "",
					order = 6,
				),
				Material(
					id = "5",
					name = "Material Object Test",
					subCategory = MaterialSubCategory.MISCELLANEOUS.toString(),
					subType = MaterialSubType.ITEM.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = 5,
				),

				)
			val creatureDropTrophy = listOf(
				Material(
					id = "7",
					name = "Material Object Test",
					subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
					subType = MaterialSubType.TROPHY.toString(),
					imageUrl = "",
					category = AppCategory.MATERIAL.toString(),
					description = "",
					order = Random.nextInt(0, 100),
				)
			)
			val materials = creatureDrop + miscList + creatureDropTrophy
			whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(materials))

			val viewModel = MaterialListViewModel(
				materialUseCases = materialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = UnconfinedTestDispatcher()
			)


			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.materialsUiState is UIState.Loading)

				viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
				viewModel.onEvent(MaterialUiEvent.ChipSelected(MaterialSubType.LOOT))
				val creatureDropResult = awaitItem()
				assert(creatureDropResult.materialsUiState is UIState.Success)
				assertEquals(MaterialSubCategory.CREATURE_DROP, creatureDropResult.selectedCategory)
				assertEquals(creatureDropResult.selectedChip, MaterialSubType.LOOT)

				viewModel.onEvent(MaterialUiEvent.ChipSelected(MaterialSubType.TROPHY))
				val creatureDropTrophyResult = awaitItem()
				when (creatureDropTrophyResult.materialsUiState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {

						assertEquals(creatureDropTrophyResult.selectedChip, MaterialSubType.TROPHY)
						val expectedMiscData = creatureDropTrophy.sortedBy { it.name }
						assertEquals(
							expectedMiscData,
							creatureDropTrophyResult.materialsUiState.data
						)
						assertEquals(1, creatureDropTrophyResult.materialsUiState.data.size)
					}
				}
			}
		}

	@Test
	fun onEvent_ChipSelectedSameChip_ShouldDeselectChip() = runTest {
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		val materials = listOf(
			Material(
				id = "1",
				name = "Test Material",
				subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
				subType = MaterialSubType.LOOT.toString(),
				imageUrl = "",
				category = AppCategory.MATERIAL.toString(),
				description = "",
				order = 1,
			)
		)
		whenever(materialUseCases.getLocalMaterials()).thenReturn(flowOf(materials))

		val viewModel = MaterialListViewModel(
			materialUseCases = materialUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = UnconfinedTestDispatcher()
		)

		viewModel.uiState.test {
			val initLoad = awaitItem()
			assert(initLoad.materialsUiState is UIState.Loading)

			// Select category first
			viewModel.onEvent(MaterialUiEvent.CategorySelected(MaterialSubCategory.CREATURE_DROP))
			val categoryResult = awaitItem()
			assert(categoryResult.materialsUiState is UIState.Success)
			assertNull(categoryResult.selectedChip)

			// Select a chip
			viewModel.onEvent(MaterialUiEvent.ChipSelected(MaterialSubType.LOOT))
			val chipSelectedResult = awaitItem()
			assertEquals(MaterialSubType.LOOT, chipSelectedResult.selectedChip)

			// Select the same chip again (should deselect)
			viewModel.onEvent(MaterialUiEvent.ChipSelected(MaterialSubType.LOOT))
			val chipDeselectedResult = awaitItem()
			assertNull(chipDeselectedResult.selectedChip) // Should be null now

			// Should show all materials in category, not filtered by chip
			assert(chipDeselectedResult.materialsUiState is UIState.Success)
			assertEquals(1, (chipDeselectedResult.materialsUiState as UIState.Success).data.size)
		}
	}

	@Test
	fun getLabelFor_ValidSubCategory_ShouldReturnCorrectLabel() {
		val viewModel = MaterialListViewModel(
			materialUseCases = materialUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)

		val creatureDropLabel = viewModel.getLabelFor(MaterialSubCategory.CREATURE_DROP)
		val miscLabel = viewModel.getLabelFor(MaterialSubCategory.MISCELLANEOUS)

		assertNotNull(creatureDropLabel)
		assertNotNull(miscLabel)

	}

}