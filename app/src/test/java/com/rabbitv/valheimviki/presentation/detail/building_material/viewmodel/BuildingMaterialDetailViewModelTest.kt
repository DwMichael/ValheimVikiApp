package com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class BuildingMaterialDetailViewModelTest {
	private val testBuildingMaterial = BuildingMaterial(
		id = "buildingMaterial1",
		category = AppCategory.BUILDING_MATERIAL.toString(),
		subCategory = "",
		imageUrl = "test_image.png",
		name = "Test Building Material",
		description = "Test building material description",
		order = 1
	)
	private val testFood = Food(
		id = "f1", category = AppCategory.FOOD.toString(), subCategory = "",
		imageUrl = "", name = "F1", description = "", order = 2
	)

	private val testMaterial = Material(
		id = "m1", category = AppCategory.MATERIAL.toString(), subCategory = "",
		imageUrl = "", name = "M1", description = "", order = 2
	)

	private val craftingObject1 = CraftingObject(
		id = "cO1", category = AppCategory.CRAFTING.toString(), subCategory = "",
		imageUrl = "", name = "CO1", description = "", order = 1
	)

	private val craftingObject2 = CraftingObject(
		id = "cO2", category = AppCategory.CRAFTING.toString(), subCategory = "",
		imageUrl = "", name = "CO2", description = "", order = 2
	)

	private val testRelations = listOf(
		RelatedItem(
			id = "buildingMaterial1"
		),
		RelatedItem(
			id = "m1",
			quantity = 1,
			quantity2star = 2,
			quantity3star = 3,
			quantity4star = 4,
			chance1star = 45,
			chance2star = 32,
			chance3star = 15
		),
		RelatedItem(
			id = "f1", quantity = 34, chance1star = 45
		),
		RelatedItem(
			id = "cO1"
		),
		RelatedItem(id = "cO2"),
	)
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var buildingMaterialUseCases: BuildMaterialUseCases

	@Mock
	private lateinit var materialUseCases: MaterialUseCases

	@Mock
	private lateinit var craftingUseCases: CraftingObjectUseCases

	@Mock
	private lateinit var foodUseCases: FoodUseCases

	@Mock
	private lateinit var relationsUseCases: RelationUseCases

	@Mock
	private lateinit var favoriteUseCases: FavoriteUseCases


	private lateinit var savedStateHandle: SavedStateHandle

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		savedStateHandle = SavedStateHandle(
			mapOf(
				"buildingMaterialId" to testBuildingMaterial.id
			)
		)
		favoriteUseCases = FavoriteUseCases(
			isFavorite = mock(),
			getAllFavoritesUseCase = mock(),
			deleteFavoriteUseCase = mock(),
			addFavoriteUseCase = mock(),
			toggleFavoriteUseCase = mock(),
		)

		relationsUseCases = RelationUseCases(
			getRelatedIdsForUseCase = mock(),
			getLocalRelationsUseCase = mock(),
			getRelatedIdUseCase = mock(),
			getRelatedIdsUseCase = mock(),
		)
		materialUseCases = MaterialUseCases(
			getLocalMaterials = mock(),
			getMaterialsByIds = mock(),
			getMaterialById = mock(),
			getMaterialsBySubCategory = mock(),
			getMaterialsBySubCategoryAndSubType = mock(),
		)
		favoriteUseCases = FavoriteUseCases(
			isFavorite = mock(),
			getAllFavoritesUseCase = mock(),
			deleteFavoriteUseCase = mock(),
			addFavoriteUseCase = mock(),
			toggleFavoriteUseCase = mock(),
		)
		buildingMaterialUseCases = BuildMaterialUseCases(
			getLocalBuildMaterial = mock(),
			getBuildMaterialByIds = mock(),
			getBuildMaterialById = mock(),
			getBuildMaterialsBySubCategory = mock(),
			getBuildMaterialsBySubCategoryAndSubType = mock(),
		)
		craftingUseCases = CraftingObjectUseCases(
			getCraftingObjectById = mock(),
			getCraftingObjectsByIds = mock(),
			getCraftingObjectByIds = mock(),
			getLocalCraftingObjectsUseCase = mock(),
			getCraftingObjectsBySubCategoryUseCase = mock(),
		)
		foodUseCases = FoodUseCases(
			getFoodBySubCategoryUseCase = mock(),
			getFoodListByIdsUseCase = mock(),
			getFoodByIdUseCase = mock(),
			getLocalFoodListUseCase = mock(),
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()

	}

	private fun buildingMaterialViewModel() =
		BuildingMaterialDetailViewModel(
			savedStateHandle = savedStateHandle,
			relationsUseCases = relationsUseCases,
			materialUseCases = materialUseCases,
			favoriteUseCases = favoriteUseCases,
			buildingMaterialUseCases = buildingMaterialUseCases,
			craftingUseCases = craftingUseCases,
			foodUseCases = foodUseCases,
			defaultDispatcher = testDispatcher,
		)

	private fun emitValuesFromUseCases(
		buildingMaterial: BuildingMaterial? = testBuildingMaterial,
		relations: List<RelatedItem> = testRelations,
		crafting: List<CraftingObject> = emptyList(),
		materials: List<Material> = emptyList(),
		food: List<Food> = emptyList(),
		isFav: Boolean = false,
	) {
		whenever(buildingMaterialUseCases.getBuildMaterialById(any())).thenReturn(
			flowOf(
				buildingMaterial
			)
		)
		whenever(relationsUseCases.getRelatedIdsUseCase(any())).thenReturn(flowOf(relations))
		whenever(materialUseCases.getMaterialsByIds(any())).thenReturn(flowOf(materials))
		whenever(craftingUseCases.getCraftingObjectsByIds(any())).thenReturn(flowOf(crafting))
		whenever(foodUseCases.getFoodListByIdsUseCase(any())).thenReturn(flowOf(food))
		whenever(favoriteUseCases.isFavorite(any())).thenReturn(flowOf(isFav))
	}

	@Test
	fun initial_EmissionContentEmpty_ShouldEmitEmptyValues() = runTest {
		emitValuesFromUseCases()

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()
			assertEquals(testBuildingMaterial.id, state.buildingMaterial!!.id)
			assertTrue(state.craftingStation is UIState.Success, "crafting should be Success")
			assertEquals(emptyList(), state.craftingStation.data)
			assertTrue(state.materials is UIState.Success, "materials should be Success")
			assertEquals(emptyList(), state.materials.data)
			assertTrue(state.foods is UIState.Success, "food should be Success")
			assertEquals(emptyList(), state.foods.data)
			assertFalse(state.isFavorite, "crafting should be null")

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_DataFetched_ShouldEmitSuccessWithData() = runTest {

		emitValuesFromUseCases(
			relations = testRelations,
			crafting = listOf(craftingObject1, craftingObject2),
			materials = listOf(testMaterial),
			food = listOf(testFood),
			isFav = true
		)

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			val state = awaitItem()


			assertEquals(testBuildingMaterial.id, state.buildingMaterial!!.id)
			when (val craftingState = state.craftingStation) {
				is UIState.Error -> fail("Should not emit error for craftingStation")
				is UIState.Loading -> fail("Should not emit loading for craftingStation")
				is UIState.Success -> {
					assertEquals(2, craftingState.data.size, "Should find 2 crafting stations")
					assertEquals(craftingObject1.id, craftingState.data[0].id)
					assertEquals(craftingObject2.id, craftingState.data[1].id)
				}
			}

			when (val materialsState = state.materials) {
				is UIState.Error -> fail("Should not emit error for materials")
				is UIState.Loading -> fail("Should not emit loading for materials")
				is UIState.Success -> {
					assertEquals(1, materialsState.data.size, "Should find 1 material")
					assertEquals(testMaterial.id, materialsState.data[0].itemDrop.id)
				}
			}

			when (val foodsState = state.foods) {
				is UIState.Error -> fail("Should not emit error for foods")
				is UIState.Loading -> fail("Should not emit loading for foods")
				is UIState.Success -> {
					assertEquals(1, foodsState.data.size, "Should find 1 food")
				}
			}
			assertTrue(state.isFavorite, "isFavorite should be true")

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_FavoriteStatusTrue_ShouldEmitTrueInState() = runTest {
		emitValuesFromUseCases(isFav = true)

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()

			assertTrue(state.isFavorite)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_FavoriteStatusFalse_ShouldEmitFalseInState() = runTest {
		emitValuesFromUseCases(isFav = false)

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()

			assertFalse(state.isFavorite)

			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun uiState_MaterialsWithQuantities_ShouldEmitCorrectQuantityData() = runTest {
		val materialRelation = RelatedItem(
			id = "m1",
			quantity = 5,
			quantity2star = 10,
			quantity3star = 15,
		)
		emitValuesFromUseCases(
			relations = listOf(materialRelation),
			materials = listOf(testMaterial)
		)

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()

			when (val materialsState = state.materials) {
				is UIState.Error -> fail("Should not emit error for materials")
				is UIState.Loading -> fail("Should not emit loading for materials")
				is UIState.Success -> {
					val materialData = materialsState.data[0]
					assertEquals(listOf(5, 10, 15), materialData.quantityList)

				}
			}
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_FoodWithQuantities_ShouldEmitCorrectQuantityData() = runTest {
		val foodRelation = RelatedItem(
			id = "f1",
			quantity = 25,
			quantity2star = 10,
			quantity3star = 15,
		)
		emitValuesFromUseCases(
			relations = listOf(foodRelation),
			food = listOf(testFood)
		)

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()

			when (val foodState = state.foods) {
				is UIState.Error -> fail("Should not emit error for materials")
				is UIState.Loading -> fail("Should not emit loading for materials")
				is UIState.Success -> {
					val foodData = foodState.data[0]
					assertEquals(listOf(25, 10, 15), foodData.quantityList)

				}
			}
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_EmptyRelations_ShouldEmitAllEmptyCollections() = runTest {
		emitValuesFromUseCases(relations = emptyList())

		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			val state = awaitItem()
			advanceUntilIdle()

			assertTrue(state.materials is UIState.Success)
			assertEquals(0, state.materials.data.size)
			assertTrue(state.foods is UIState.Success)
			assertEquals(0, state.foods.data.size)
			assertTrue(state.craftingStation is UIState.Success)
			assertEquals(0, state.craftingStation.data.size)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiEvent_ToggleFavoriteWhenNotFavorite_ShouldCallToggleUseCaseWithTrue() = runTest {
		emitValuesFromUseCases(isFav = false)
		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			advanceUntilIdle()

			vm.uiEvent(BuildingMaterialUiEvent.ToggleFavorite)
			advanceUntilIdle()

			verify(favoriteUseCases.toggleFavoriteUseCase).invoke(
				favorite = testBuildingMaterial.toFavorite(),
				shouldBeFavorite = true
			)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiEvent_ToggleFavoriteWhenFavorite_ShouldCallToggleUseCaseWithFalse() = runTest {
		emitValuesFromUseCases(isFav = true)
		val vm = buildingMaterialViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			advanceUntilIdle()

			vm.uiEvent(BuildingMaterialUiEvent.ToggleFavorite)
			advanceUntilIdle()

			verify(favoriteUseCases.toggleFavoriteUseCase).invoke(
				favorite = testBuildingMaterial.toFavorite(),
				shouldBeFavorite = false
			)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiEvent_ToggleFavoriteWhenBuildingMaterialNull_ShouldNotCallToggleUseCase() = runTest {
		whenever(buildingMaterialUseCases.getBuildMaterialById(any())).thenReturn(
			flowOf(null)
		)
		whenever(relationsUseCases.getRelatedIdsUseCase(any())).thenReturn(flowOf(emptyList()))
		whenever(favoriteUseCases.isFavorite(any())).thenReturn(flowOf(false))

		val vm = buildingMaterialViewModel()

		vm.uiEvent(BuildingMaterialUiEvent.ToggleFavorite)
		advanceUntilIdle()

		verify(favoriteUseCases.toggleFavoriteUseCase, never()).invoke(any(), any())
	}
}