package com.rabbitv.valheimviki.presentation.building_material.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_material_by_id.GetBuildMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_ids.GetBuildMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory.GetBuildMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory_and_subtype.GetBuildMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_local_building_materials.GetLocalBuildMaterialsUseCase
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.DefaultAsserter.assertEquals
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
			assertEquals(UIState.Loading, awaitItem().materialsUiState)
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
			assertEquals(null, result.selectedCategory)
			assertEquals(null, result.selectedChip)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun buildingMaterialListViewModel_uiState_emitsSuccessWhenDataIsAvailable_categoryIsWOOD_chipIsNull() =
		runTest {
			val fakeBuildingMaterialList: List<BuildingMaterial> = List(4) {
				BuildingMaterial(
					id = "1",
					imageUrl = "",
					category = "",
					subCategory = BuildingMaterialSubCategory.WOOD.toString(),
					name = "Wood Item",
					description = "",
					comfortLevel = 3,
					order = 1,
					subType = "",
				)
			}

			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			whenever(buildingMaterialUseCases.getLocalBuildMaterial()).thenReturn(
				flowOf(
					fakeBuildingMaterialList
				)
			)

			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases,
				connectivityObserver,
				UnconfinedTestDispatcher(testScheduler)
			)

			viewModel.uiState.test {
				skipItems(2)

				viewModel.onEvent(
					BuildingMaterialUiEvent.CategorySelected(
						BuildingMaterialSubCategory.WOOD
					)
				)

				val result = awaitItem()

				when (val uiState = result.materialsUiState) {
					is UIState.Error -> fail("State should not be Error, but was: ${uiState.message}")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(
							"Selected category should be WOOD",
							BuildingMaterialSubCategory.WOOD,
							result.selectedCategory
						)
						assertEquals(
							"Selected chip should be null",
							null,
							result.selectedChip
						)
						assertEquals(
							"Data in success state should match the fake list",
							fakeBuildingMaterialList,
							uiState.data
						)
					}
				}
				cancelAndIgnoreRemainingEvents()
			}
		}

	@Test
	fun buildingMaterialListViewModel_uiState_emitsSuccessWhenDataIsAvailableWithNoConnection_categoryIsWOOD_chipIsNull() =
		runTest {
			val fakeBuildingMaterialList = listOf(
				BuildingMaterial(
					id = "1",
					name = "Wood Wall",
					subCategory = BuildingMaterialSubCategory.WOOD.toString(),
					order = 1,
					imageUrl = "",
					category = "",
					description = "",
					comfortLevel = 0,
					subType = ""
				),
				BuildingMaterial(
					id = "2",
					name = "Wood Floor",
					subCategory = BuildingMaterialSubCategory.WOOD.toString(),
					order = 2,
					imageUrl = "",
					category = "",
					description = "",
					comfortLevel = 0,
					subType = ""
				)
			)

			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			whenever(buildingMaterialUseCases.getLocalBuildMaterial()).thenReturn(
				flowOf(
					fakeBuildingMaterialList
				)
			)

			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases = buildingMaterialUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = UnconfinedTestDispatcher(testScheduler)
			)

			viewModel.uiState.test {
				skipItems(2)

				viewModel.onEvent(
					BuildingMaterialUiEvent.CategorySelected(
						BuildingMaterialSubCategory.WOOD
					)
				)

				val resultState = awaitItem()

				assertEquals(BuildingMaterialSubCategory.WOOD, resultState.selectedCategory)
				assertEquals(null, resultState.selectedChip)

				when (val materialsState = resultState.materialsUiState) {
					is UIState.Success -> {
						assertEquals(fakeBuildingMaterialList, materialsState.data)
					}

					is UIState.Error -> fail("UIState should be Success, but was Error: ${materialsState.message}")
					is UIState.Loading -> fail("UIState should be Success, but was Loading")
				}

				cancelAndIgnoreRemainingEvents()
			}
		}


	@Test
	fun buildingMaterialListViewModel_materialListFlow_handlesEmptyBuildingMaterialsList() =
		runTest {

			whenever(getLocalBuildMaterial()).thenReturn(
				flowOf(
					emptyList()
				)
			)
			val viewModel = BuildingMaterialListViewModel(
				buildingMaterialUseCases,
				connectivityObserver,
				Dispatchers.Default
			)
			val result = viewModel.filteredBuildingMaterialsWithSelection.first()
			assertTrue(result.component1().isEmpty())
		}

	@Test
	fun buildingMaterialListViewModel_materialsFlow_handlesCategorySelectionChanges() = runTest {
		val fakeBuildingMaterialList: List<BuildingMaterial> =
			BuildingMaterialSubCategory.entries.mapIndexed { index, subCategoryEnum ->
				BuildingMaterial(
					id = "material-${subCategoryEnum.name.lowercase()}-${index + 1}",
					imageUrl = "https://example.com/${subCategoryEnum.name.lowercase()}.jpg",
					category = "BUILDING_MATERIAL",
					subCategory = subCategoryEnum.name,
					name = "${subCategoryEnum.name} Material Example",
					description = "A generic description for ${subCategoryEnum.name} material.",
					comfortLevel = (index % 5) + 1,
					order = index + 1,
					subType = "Default ${subCategoryEnum.name} Type",
				)
			}

		whenever(getLocalBuildMaterial()).thenReturn(
			flowOf(fakeBuildingMaterialList)
		)

		val viewModel = BuildingMaterialListViewModel(
			buildingMaterialUseCases,
			connectivityObserver,
			Dispatchers.Default
		)

		viewModel.filteredBuildingMaterialsWithSelection.test {

			val (materials, _, _) = awaitItem()
			assertEquals(0, materials.size)
			assertEquals(emptyList<BuildingMaterial>(), materials)
			assertEquals(null, viewModel.uiState.value.selectedCategory)
			assertEquals(null, viewModel.uiState.value.selectedChip)

			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.WOOD))
			val woodTriple = awaitItem()
			assertEquals(1, woodTriple.component1().size)
			assertTrue(
				woodTriple.component1()
					.all { it.subCategory == BuildingMaterialSubCategory.WOOD.toString() })

			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.LIGHT_SOURCE))
			val lightTriple = awaitItem()
			assertEquals(1, lightTriple.component1().size)
			assertTrue(
				lightTriple.component1()
					.all { it.subCategory == BuildingMaterialSubCategory.LIGHT_SOURCE.toString() })

			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.ROOF))
			val roofTriple = awaitItem()
			assertEquals(1, roofTriple.component1().size)
			assertTrue(
				roofTriple.component1()
					.all { it.subCategory == BuildingMaterialSubCategory.ROOF.toString() })

			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.TRANSPORT))
			val transportTriple = awaitItem()
			assertEquals(1, transportTriple.component1().size)
			assertTrue(
				transportTriple.component1()
					.all { it.subCategory == BuildingMaterialSubCategory.TRANSPORT.toString() })


			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.RESOURCE))
			val resourceTriple = awaitItem()
			assertEquals(1, resourceTriple.component1().size)
			assertTrue(
				resourceTriple.component1()
					.all { it.subCategory == BuildingMaterialSubCategory.RESOURCE.toString() })

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun buildingMaterialListViewModel_materialsFlow_handlesChipSelectionChanges() = runTest {

		val fakeBuildingMaterialList: List<BuildingMaterial> = listOf(
			BuildingMaterial(
				id = "1",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 1,
				subType = BuildingMaterialSubType.STORAGE.toString(),
			),
			BuildingMaterial(
				id = "2",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 2,
				order = 2,
				subType = BuildingMaterialSubType.FUNCTIONAL.toString(),
			),
			BuildingMaterial(
				id = "3",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 3,
				subType = BuildingMaterialSubType.TABLE.toString(),
			),
			BuildingMaterial(
				id = "4",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 4,
				subType = BuildingMaterialSubType.RUG.toString(),
			),
			BuildingMaterial(
				id = "5",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 5,
				subType = BuildingMaterialSubType.BANNER.toString(),
			),
			BuildingMaterial(
				id = "6",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 6,
				subType = BuildingMaterialSubType.DECORATIVE.toString(),
			),
			BuildingMaterial(
				id = "7",
				imageUrl = "",
				category = "BUILDING_MATERIAL",
				subCategory = BuildingMaterialSubCategory.FURNITURE.toString(),
				name = " Material Example",
				description = "A generic description for material.",
				comfortLevel = 1,
				order = 7,
				subType = BuildingMaterialSubType.DECORATIVE.toString(),
			),
		)

		whenever(getLocalBuildMaterial()).thenReturn(
			flowOf(fakeBuildingMaterialList)
		)

		val viewModel = BuildingMaterialListViewModel(
			buildingMaterialUseCases,
			connectivityObserver,
			Dispatchers.Default
		)

		viewModel.filteredBuildingMaterialsWithSelection.test {

			val (initialMaterials, initialCategory, initialType) = awaitItem()
			assertEquals(0, initialMaterials.size)
			assertEquals(emptyList<BuildingMaterial>(), initialMaterials)
			assertEquals(null, initialCategory)
			assertEquals(null, initialType)


			viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(BuildingMaterialSubCategory.FURNITURE))

			val (furnitureMaterialList, furnitureCategory, furnitureType) = awaitItem()
			assertEquals(7, furnitureMaterialList.size)
			assertTrue(furnitureMaterialList.all { it.subCategory == BuildingMaterialSubCategory.FURNITURE.toString() })
			assertEquals(BuildingMaterialSubCategory.FURNITURE, furnitureCategory)
			assertEquals(null, furnitureType)


			viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(BuildingMaterialSubType.BANNER))

			val (furnitureBannerMaterialList, bannerCategory, bannerType) = awaitItem()
			assertEquals(1, furnitureBannerMaterialList.size)
			assertTrue(furnitureBannerMaterialList.all { it.subType == BuildingMaterialSubType.BANNER.toString() })
			assertEquals(BuildingMaterialSubCategory.FURNITURE, bannerCategory)
			assertEquals(BuildingMaterialSubType.BANNER, bannerType)


			viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(BuildingMaterialSubType.TABLE))

			val (furnitureTableMaterialList, tableCategory, tableType) = awaitItem()
			assertEquals(
				1,
				furnitureTableMaterialList.size,
				"furnitureTableMaterialList should be 1"
			)
			assertTrue(furnitureTableMaterialList.all { it.subType == BuildingMaterialSubType.TABLE.toString() })
			assertEquals(BuildingMaterialSubCategory.FURNITURE, tableCategory)
			assertEquals(BuildingMaterialSubType.TABLE, tableType)


			viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(BuildingMaterialSubType.TABLE))

			val (furnitureAllMaterialsAfterDeselect, deselectCategory, deselectType) = awaitItem()
			assertEquals(7, furnitureAllMaterialsAfterDeselect.size)
			assertTrue(furnitureAllMaterialsAfterDeselect.all { it.subCategory == BuildingMaterialSubCategory.FURNITURE.toString() })
			assertEquals(BuildingMaterialSubCategory.FURNITURE, deselectCategory)
			assertEquals(null, deselectType)


			viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(BuildingMaterialSubType.DECORATIVE))

			val (furnitureDecorativeMaterialList, decorativeCategory, decorativeType) = awaitItem()
			assertEquals(
				2,
				furnitureDecorativeMaterialList.size,
				"furnitureDecorativeMaterialList should be 2"
			)
			assertTrue(furnitureDecorativeMaterialList.all { it.subType == BuildingMaterialSubType.DECORATIVE.toString() })
			assertEquals(BuildingMaterialSubCategory.FURNITURE, decorativeCategory)
			assertEquals(BuildingMaterialSubType.DECORATIVE, decorativeType)


			cancelAndIgnoreRemainingEvents()
		}
	}
}