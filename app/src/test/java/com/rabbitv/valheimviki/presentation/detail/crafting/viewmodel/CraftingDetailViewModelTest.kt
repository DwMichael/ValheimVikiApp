package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class CraftingDetailViewModelTest {
	private val testCraftingObject = CraftingObject(
		id = "craftingObject1",
		category = AppCategory.CRAFTING.toString(),
		subCategory = "",
		imageUrl = "test_image.png",
		name = "Test Crafting Object",
		description = "Test crafting object description",
		order = 1
	)

	private val fakeCraftingObjects = listOf(
		CraftingObject(
			id = "crafting1",
			category = AppCategory.CRAFTING.toString(),
			subCategory = "workbench",
			imageUrl = "crafting1.png",
			name = "Workbench",
			description = "Basic crafting station",
			order = 1
		),
		CraftingObject(
			id = "crafting2",
			category = AppCategory.CRAFTING.toString(),
			subCategory = "forge",
			imageUrl = "crafting2.png",
			name = "Forge",
			description = "Advanced crafting station",
			order = 2
		)
	)

	private val fakeMaterials = listOf(
		Material(
			id = "wood",
			category = AppCategory.MATERIAL.toString(),
			subCategory = "basic",
			imageUrl = "wood.png",
			name = "Wood",
			description = "Basic building material",
			order = 1
		),
		Material(
			id = "stone",
			category = AppCategory.MATERIAL.toString(),
			subCategory = "basic",
			imageUrl = "stone.png",
			name = "Stone",
			description = "Hard building material",
			order = 2
		),
		Material(
			id = "iron",
			category = AppCategory.MATERIAL.toString(),
			subCategory = "metal",
			imageUrl = "iron.png",
			name = "Iron",
			description = "Strong metal material",
			order = 3
		)
	)

	private val fakeFood = listOf(
		Food(
			id = "cooked_meat",
			category = AppCategory.FOOD.toString(),
			subCategory = "meat",
			imageUrl = "cooked_meat.png",
			name = "Cooked Meat",
			description = "Nutritious cooked meat",
			order = 1
		),
		Food(
			id = "bread",
			category = AppCategory.FOOD.toString(),
			subCategory = "baked",
			imageUrl = "bread.png",
			name = "Bread",
			description = "Basic bread for sustenance",
			order = 2
		)
	)

	private val fakeMeads = listOf(
		Mead(
			id = "healing_mead",
			category = AppCategory.MEAD.toString(),
			subCategory = "mead",
			imageUrl = "healing_mead.png",
			name = "Healing Mead",
			description = "Restores health over time",
			recipeOutput = "4",
			effect = "Heal 75 hp over 10s",
			duration = "10s",
			cooldown = "2s",
			order = 1
		),
		Mead(
			id = "stamina_mead",
			category = AppCategory.MEAD.toString(),
			subCategory = "mead",
			imageUrl = "stamina_mead.png",
			name = "Stamina Mead",
			description = "Restores stamina over time",
			recipeOutput = "4",
			effect = "Heal 80 stamina over 10s",
			duration = "10s",
			cooldown = "2s",
			order = 2
		)
	)

	private val fakeWeapons = listOf(
		Weapon(
			id = "bronze_sword",
			category = AppCategory.WEAPON.toString(),
			subCategory = "sword",
			imageUrl = "bronze_sword.png",
			name = "Bronze Sword",
			description = "Sharp bronze blade",
			order = 1,
			upgradeInfoList = emptyList(),
			subType = "one_handed"
		),
		Weapon(
			id = "iron_axe",
			category = AppCategory.WEAPON.toString(),
			subCategory = "axe",
			imageUrl = "iron_axe.png",
			name = "Iron Axe",
			description = "Heavy iron axe",
			order = 2,
			upgradeInfoList = emptyList(),
			subType = "one_handed"
		)
	)

	private val fakeArmors = listOf(
		Armor(
			id = "leather_helmet",
			category = AppCategory.ARMOR.toString(),
			subCategory = "helmet",
			imageUrl = "leather_helmet.png",
			name = "Leather Helmet",
			description = "Basic leather protection",
			upgradeInfoList = emptyList(),
			effects = "Armor +2",
			usage = "Head protection",
			fullSetEffects = null,
			order = 1
		),
		Armor(
			id = "bronze_chest",
			category = AppCategory.ARMOR.toString(),
			subCategory = "chest",
			imageUrl = "bronze_chest.png",
			name = "Bronze Plate Cuirass",
			description = "Bronze chest armor",
			upgradeInfoList = emptyList(),
			effects = "Armor +8",
			usage = "Chest protection",
			fullSetEffects = null,
			order = 2
		)
	)

	private val fakeTools = listOf(
		ItemTool(
			id = "stone_axe",
			category = AppCategory.TOOL.toString(),
			subCategory = "axe",
			imageUrl = "stone_axe.png",
			name = "Stone Axe",
			description = "Basic stone cutting tool",
			howToUse = "Left click to chop trees",
			generalInfo = "Used for chopping wood",
			upgradeInfoList = emptyList(),
			order = 1
		),
		ItemTool(
			id = "copper_pickaxe",
			category = AppCategory.TOOL.toString(),
			subCategory = "pickaxe",
			imageUrl = "copper_pickaxe.png",
			name = "Copper Pickaxe",
			description = "Copper mining tool",
			howToUse = "Left click to mine",
			generalInfo = "Used for mining copper and tin",
			upgradeInfoList = emptyList(),
			order = 2
		)
	)

	private val fakeBuildingMaterials = listOf(
		BuildingMaterial(
			id = "wooden_wall",
			category = AppCategory.BUILDING_MATERIAL.toString(),
			subCategory = "wall",
			imageUrl = "wooden_wall.png",
			name = "Wooden Wall",
			description = "Basic wooden wall segment",
			comfortLevel = null,
			order = 1,
			subType = "wood"
		),
		BuildingMaterial(
			id = "stone_floor",
			category = AppCategory.BUILDING_MATERIAL.toString(),
			subCategory = "floor",
			imageUrl = "stone_floor.png",
			name = "Stone Floor",
			description = "Durable stone flooring",
			comfortLevel = null,
			order = 2,
			subType = "stone"
		),
		BuildingMaterial(
			id = "bed",
			category = AppCategory.BUILDING_MATERIAL.toString(),
			subCategory = "furniture",
			imageUrl = "bed.png",
			name = "Bed",
			description = "Comfortable sleeping spot",
			comfortLevel = 2,
			order = 3,
			subType = "furniture"
		)
	)

	private val testRelations = listOf(
		RelatedItem(
			id = "wooden_wall"
		),
		RelatedItem(
			id = "wood",
			quantity = 1,
			quantity2star = 2,
			quantity3star = 3,
			quantity4star = 4,
			chance1star = 45,
			chance2star = 32,
			chance3star = 15
		),
		RelatedItem(
			id = "cooked_meat",
			quantity = 34,
			chance1star = 45
		),
		RelatedItem(
			id = "crafting1"
		),
		RelatedItem(
			id = "crafting2"
		),
		RelatedItem(
			id = "healing_mead"
		),
		RelatedItem(
			id = "bronze_sword"
		),
		RelatedItem(
			id = "leather_helmet"
		),
		RelatedItem(
			id = "stone_axe"
		)
	)
	private val testDispatcher = StandardTestDispatcher()


	@Mock
	private lateinit var craftingUseCases: CraftingObjectUseCases

	@Mock
	private lateinit var relationsUseCases: RelationUseCases

	@Mock
	private lateinit var foodUseCases: FoodUseCases

	@Mock
	private lateinit var meadUseCases: MeadUseCases

	@Mock
	private lateinit var materialUseCases: MaterialUseCases

	@Mock
	private lateinit var weaponUseCases: WeaponUseCases

	@Mock
	private lateinit var armorUseCase: ArmorUseCases

	@Mock
	private lateinit var toolsUseCase: ToolUseCases

	@Mock
	private lateinit var buildingMaterialUseCases: BuildMaterialUseCases

	@Mock
	private lateinit var favoriteUseCases: FavoriteUseCases


	private lateinit var savedStateHandle: SavedStateHandle

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		savedStateHandle = SavedStateHandle(
			mapOf(
				"craftingObjectId" to testCraftingObject.id
			)
		)
		favoriteUseCases = FavoriteUseCases(
			isFavorite = mock(),
			getAllFavoritesUseCase = mock(),
			deleteFavoriteUseCase = mock(),
			addFavoriteUseCase = mock(),
			toggleFavoriteUseCase = mock(),
		)
		meadUseCases = MeadUseCases(
			getLocalMeadsUseCase = mock(),
			getMeadByIdUseCase = mock(),
			getMeadsByIdsUseCase = mock(),
			getMeadsBySubCategoryUseCase = mock(),
		)

		weaponUseCases = WeaponUseCases(
			getLocalWeaponsUseCase = mock(),
			getWeaponsByIdsUseCase = mock(),
			getWeaponByIdUseCase = mock(),
			getWeaponsBySubCategoryUseCase = mock(),
			getWeaponsBySubTypeUseCase = mock()
		)
		armorUseCase = ArmorUseCases(
			getLocalArmorsUseCase = mock(),
			getArmorByIdUseCase = mock(),
			getArmorsByIdsUseCase = mock()
		)
		toolsUseCase = ToolUseCases(
			getLocalToolsUseCase = mock(),
			getToolByIdUseCase = mock(),
			getToolsByIdsUseCase = mock(),
			getToolsBySubCategoryUseCase = mock()
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
			getMaterialsBySubCategoryAndIds = mock()
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

	private fun craftingDetailViewModel() =
		CraftingDetailViewModel(
			savedStateHandle = savedStateHandle,
			favoriteUseCases = favoriteUseCases,
			defaultDispatcher = testDispatcher,
			_craftingObjectUseCases = craftingUseCases,
			_relationsUseCases = relationsUseCases,
			_foodUseCase = foodUseCases,
			_meadUseCase = meadUseCases,
			_materialUseCase = materialUseCases,
			_weaponUseCase = weaponUseCases,
			_armorUseCase = armorUseCase,
			_toolsUseCase = toolsUseCase,
			_buildIngMaterials = buildingMaterialUseCases,
		)

	private fun emitValuesFromUseCases(
		craftingObject: CraftingObject? = testCraftingObject,
		relations: List<RelatedItem> = testRelations,
		crafting: List<CraftingObject> = fakeCraftingObjects,
		materials: List<Material> = fakeMaterials,
		food: List<Food> = fakeFood,
		meads: List<Mead> = fakeMeads,
		weapons: List<Weapon> = fakeWeapons,
		armors: List<Armor> = fakeArmors,
		tools: List<ItemTool> = fakeTools,
		buildingMaterials: List<BuildingMaterial> = fakeBuildingMaterials,
		isFav: Boolean = false,
		errorTest: Boolean = false
	) {
		whenever(relationsUseCases.getRelatedIdsUseCase(any())).thenReturn(flowOf(relations))
		whenever(craftingUseCases.getCraftingObjectById(any())).thenReturn(flowOf(craftingObject))
		whenever(materialUseCases.getMaterialsByIds(any())).thenReturn(flowOf(materials))
		whenever(craftingUseCases.getCraftingObjectsByIds(any())).thenReturn(flowOf(crafting))
		whenever(foodUseCases.getFoodListByIdsUseCase(any())).thenReturn(flowOf(food))


		whenever(meadUseCases.getMeadsByIdsUseCase(any())).thenReturn(flowOf(meads))
		if (errorTest) {
			whenever(weaponUseCases.getWeaponsByIdsUseCase(any())).thenReturn(flow {
				throw RuntimeException("Database error")
			})
		} else {
			whenever(weaponUseCases.getWeaponsByIdsUseCase(any())).thenReturn(flowOf(weapons))
		}

		whenever(armorUseCase.getArmorsByIdsUseCase(any())).thenReturn(flowOf(armors))
		whenever(toolsUseCase.getToolsByIdsUseCase(any())).thenReturn(flowOf(tools))
		whenever(buildingMaterialUseCases.getBuildMaterialByIds(any())).thenReturn(
			flowOf(
				buildingMaterials
			)
		)
		whenever(favoriteUseCases.isFavorite(any())).thenReturn(flowOf(isFav))
	}

	@Test
	fun uiState_InitValues_ShouldEmitInitValues() = runTest {
		val viewModel = craftingDetailViewModel()

		viewModel.uiState.test {
			val firstEmit = awaitItem()

			assertNull(firstEmit.craftingObject)
			assertTrue(firstEmit.craftingUpgraderObjects is UIState.Loading)
			assertTrue(firstEmit.craftingFoodProducts is UIState.Loading)
			assertTrue(firstEmit.craftingMeadProducts is UIState.Loading)
			assertTrue(firstEmit.craftingMaterialToBuild is UIState.Loading)
			assertTrue(firstEmit.craftingMaterialRequired is UIState.Loading)
			assertTrue(firstEmit.craftingMaterialProducts is UIState.Loading)
			assertTrue(firstEmit.craftingWeaponProducts is UIState.Loading)
			assertTrue(firstEmit.craftingArmorProducts is UIState.Loading)
			assertTrue(firstEmit.craftingToolProducts is UIState.Loading)
			assertTrue(firstEmit.craftingBuildingMaterialProducts is UIState.Loading)
			assertFalse(firstEmit.isFavorite)
		}
	}

	@Test
	fun uiState_DataFetched_ShouldEmitSuccesses() = runTest {
		emitValuesFromUseCases()
		val viewModel = craftingDetailViewModel()

		viewModel.uiState.test {
			awaitItem()
			val secondEmit = awaitItem()

			assertEquals(testCraftingObject, secondEmit.craftingObject)

			// Check that all states are Success
			assertTrue(secondEmit.craftingUpgraderObjects is UIState.Success)
			assertTrue(secondEmit.craftingFoodProducts is UIState.Success)
			assertTrue(secondEmit.craftingMeadProducts is UIState.Success)
			assertTrue(secondEmit.craftingMaterialToBuild is UIState.Success)
			assertTrue(secondEmit.craftingMaterialRequired is UIState.Success)
			assertTrue(secondEmit.craftingMaterialProducts is UIState.Success)
			assertTrue(secondEmit.craftingWeaponProducts is UIState.Success)
			assertTrue(secondEmit.craftingArmorProducts is UIState.Success)
			assertTrue(secondEmit.craftingToolProducts is UIState.Success)
			assertTrue(secondEmit.craftingBuildingMaterialProducts is UIState.Success)

			// Check the actual data by extracting from UIState.Success
			val craftingObjects = secondEmit.craftingUpgraderObjects.data
			val foodProducts = secondEmit.craftingFoodProducts.data
			val meadProducts = secondEmit.craftingMeadProducts.data
			val weaponProducts = secondEmit.craftingWeaponProducts.data
			val armorProducts = secondEmit.craftingArmorProducts.data
			val toolProducts = secondEmit.craftingToolProducts.data
			val buildingMaterialProducts = secondEmit.craftingBuildingMaterialProducts.data

			// Verify the lists contain the expected number of items
			assertEquals(2, craftingObjects.size)
			assertEquals(2, foodProducts.size)
			assertEquals(2, meadProducts.size)
			assertEquals(2, weaponProducts.size)
			assertEquals(2, armorProducts.size)
			assertEquals(2, toolProducts.size)
			assertEquals(3, buildingMaterialProducts.size)

			// Verify the actual items in CraftingProducts objects
			assertEquals("crafting1", craftingObjects[0].itemDrop.id)
			assertEquals("crafting2", craftingObjects[1].itemDrop.id)
			assertEquals("cooked_meat", foodProducts[0].itemDrop.id)
			assertEquals("healing_mead", meadProducts[0].itemDrop.id)
			assertEquals("bronze_sword", weaponProducts[0].itemDrop.id)
			assertEquals("leather_helmet", armorProducts[0].itemDrop.id)
			assertEquals("stone_axe", toolProducts[0].itemDrop.id)
			assertEquals("wooden_wall", buildingMaterialProducts[0].itemDrop.id)

			// Verify quantity information from relations
			assertEquals(listOf(34), foodProducts[0].quantityList) // from testRelations
			assertEquals(
				listOf(null),
				craftingObjects[0].quantityList
			) // no quantity in testRelations

			assertFalse(secondEmit.isFavorite)
		}
	}

	@Test
	fun uiState_WhenWeaponUseCaseFails_ShouldEmitErrorForWeapons() = runTest {
		emitValuesFromUseCases(
			errorTest = true
		)


		val viewModel = craftingDetailViewModel()

		// Act & Assert
		viewModel.uiState.test {
			awaitItem()
			val stateAfterFetch = awaitItem()

			assertTrue(stateAfterFetch.craftingWeaponProducts is UIState.Error)

			val errorMessage = (stateAfterFetch.craftingWeaponProducts as UIState.Error).message

			assertEquals("Database error", errorMessage)
			assertTrue(stateAfterFetch.craftingFoodProducts is UIState.Success)
			assertTrue(stateAfterFetch.craftingArmorProducts is UIState.Success)
			assertEquals(2, stateAfterFetch.craftingFoodProducts.data.size)
		}
	}

	@Test
	fun uiState_WhenNoRelatedWeapons_ShouldEmitSuccessWithEmptyList() = runTest {

		emitValuesFromUseCases(
			weapons = emptyList()
		)
		val viewModel = craftingDetailViewModel()


		viewModel.uiState.test {
			awaitItem()
			val state = awaitItem()

			assertTrue(state.craftingWeaponProducts is UIState.Success)
			assertTrue(state.craftingWeaponProducts.data.isEmpty())
		}
	}

	@Test
	fun uiEvent_ToggleFavorite_ShouldCallUseCase() = runTest {
		emitValuesFromUseCases(isFav = false)
		val viewModel = craftingDetailViewModel()

		viewModel.uiState.test {
			awaitItem()
			awaitItem()
			advanceUntilIdle()

			viewModel.uiEvent(CraftingDetailUiEvent.ToggleFavorite)
			advanceUntilIdle()

			verify(favoriteUseCases.toggleFavoriteUseCase).invoke(
				favorite = testCraftingObject.toFavorite(),
				shouldBeFavorite = true
			)

			cancelAndIgnoreRemainingEvents()

		}
	}
}