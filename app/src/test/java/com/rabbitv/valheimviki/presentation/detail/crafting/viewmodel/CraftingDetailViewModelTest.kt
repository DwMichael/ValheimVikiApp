package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


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
		crafting: List<CraftingObject> = emptyList(),
		materials: List<Material> = emptyList(),
		food: List<Food> = emptyList(),
		meads: List<Mead> = emptyList(),
		weapons: List<Weapon> = emptyList(),
		armors: List<Armor> = emptyList(),
		tools: List<ItemTool> = emptyList(),
		buildingMaterials: List<BuildingMaterial> = emptyList(),
		isFav: Boolean = false,
	) {
		whenever(craftingUseCases.getCraftingObjectById(any())).thenReturn(flowOf(craftingObject))
		whenever(relationsUseCases.getRelatedIdsUseCase(any())).thenReturn(flowOf(relations))
		whenever(materialUseCases.getMaterialsByIds(any())).thenReturn(flowOf(materials))
		whenever(craftingUseCases.getCraftingObjectsByIds(any())).thenReturn(flowOf(crafting))
		whenever(foodUseCases.getFoodListByIdsUseCase(any())).thenReturn(flowOf(food))


		whenever(meadUseCases.getMeadsByIdsUseCase(any())).thenReturn(flowOf(meads))
		whenever(weaponUseCases.getWeaponsByIdsUseCase(any())).thenReturn(flowOf(weapons))
		whenever(armorUseCase.getArmorsByIdsUseCase(any())).thenReturn(flowOf(armors))
		whenever(toolsUseCase.getToolsByIdsUseCase(any())).thenReturn(flowOf(tools))
		whenever(buildingMaterialUseCases.getBuildMaterialByIds(any())).thenReturn(
			flowOf(
				buildingMaterials
			)
		)

		whenever(favoriteUseCases.isFavorite(any())).thenReturn(flowOf(isFav))
	}


}