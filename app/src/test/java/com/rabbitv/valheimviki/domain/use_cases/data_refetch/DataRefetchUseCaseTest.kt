package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.data_language_provider.DataLanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_guided_onboarding_step.ReadGuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_popup_state.ReadLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_last_data_refresh_at.ReadLastSuccessfulDataRefreshAt
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_settings_tooltip_state.ReadSettingsTooltipState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_data_language_state.SaveDataLanguageState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_guided_onboarding_step.SaveGuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_language_popup_state.SaveLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_last_data_refresh_at.SaveLastSuccessfulDataRefreshAt
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_settings_tooltip_state.SaveSettingsTooltipState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import com.rabbitv.valheimviki.domain.use_cases.favorite.sync_favorite.SyncFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DataRefetchUseCaseTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var biomeRepository: BiomeRepository

	@Mock
	private lateinit var creatureRepository: CreatureRepository

	@Mock
	private lateinit var oreDepositRepository: OreDepositRepository

	@Mock
	private lateinit var materialsRepository: MaterialRepository

	@Mock
	private lateinit var relationsRepository: RelationRepository

	@Mock
	private lateinit var pointOfInterestRepository: PointOfInterestRepository

	@Mock
	private lateinit var treeRepository: TreeRepository

	@Mock
	private lateinit var foodRepository: FoodRepository

	@Mock
	private lateinit var weaponRepository: WeaponRepository

	@Mock
	private lateinit var armorRepository: ArmorRepository

	@Mock
	private lateinit var meadRepository: MeadRepository

	@Mock
	private lateinit var toolRepository: ToolRepository

	@Mock
	private lateinit var trinketRepository: TrinketRepository

	@Mock
	private lateinit var syncFavoritesUseCase: SyncFavoritesUseCase

	@Mock
	private lateinit var buildingMaterialRepository: BuildingMaterialRepository

	@Mock
	private lateinit var craftingObjectRepository: CraftingObjectRepository

	@Mock
	private lateinit var searchRepository: SearchRepository

	@Mock
	private lateinit var dataStoreUseCases: DataStoreUseCases

	private lateinit var languageProvider: LanguageProvider
	private lateinit var dataLanguageProvider: DataLanguageProvider
	private lateinit var saveDataLanguageState: SaveDataLanguageState

	private lateinit var useCase: DataRefetchUseCase

	@BeforeEach
	fun setup() {
		Dispatchers.setMain(testDispatcher)

		// Initialize mocks
		biomeRepository = mock()
		creatureRepository = mock()
		oreDepositRepository = mock()
		materialsRepository = mock()
		relationsRepository = mock()
		pointOfInterestRepository = mock()
		treeRepository = mock()
		foodRepository = mock()
		weaponRepository = mock()
		armorRepository = mock()
		meadRepository = mock()
		toolRepository = mock()
		buildingMaterialRepository = mock()
		craftingObjectRepository = mock()
		searchRepository = mock()
		languageProvider = mock()
		dataLanguageProvider = mock()
		saveDataLanguageState = mock()
		dataStoreUseCases = DataStoreUseCases(
			readOnBoardingUseCase = mock<ReadOnBoardingState>(),
			saveOnBoardingState = mock<SaveOnBoardingState>(),
			languageProvider = languageProvider,
			saveLanguageState = mock<SaveLanguageState>(),
			dataLanguageProvider = dataLanguageProvider,
			saveDataLanguageState = saveDataLanguageState,
			readLanguagePopupState = mock<ReadLanguagePopupState>(),
			saveLanguagePopupState = mock<SaveLanguagePopupState>(),
			readGuidedOnboardingStep = mock<ReadGuidedOnboardingStep>(),
			saveGuidedOnboardingStep = mock<SaveGuidedOnboardingStep>(),
			readSettingsTooltipState = mock<ReadSettingsTooltipState>(),
			saveSettingsTooltipState = mock<SaveSettingsTooltipState>(),
			readLastSuccessfulDataRefreshAt = mock<ReadLastSuccessfulDataRefreshAt>(),
			saveLastSuccessfulDataRefreshAt = mock<SaveLastSuccessfulDataRefreshAt>()
		)
		trinketRepository = mock()
		syncFavoritesUseCase = mock()

		useCase = DataRefetchUseCase(
			biomeRepository = biomeRepository,
			creatureRepository = creatureRepository,
			oreDepositRepository = oreDepositRepository,
			materialsRepository = materialsRepository,
			relationsRepository = relationsRepository,
			pointOfInterestRepository = pointOfInterestRepository,
			treeRepository = treeRepository,
			foodRepository = foodRepository,
			weaponRepository = weaponRepository,
			armorRepository = armorRepository,
			meadRepository = meadRepository,
			toolRepository = toolRepository,
			buildingMaterialRepository = buildingMaterialRepository,
			craftingObjectRepository = craftingObjectRepository,
			searchRepository = searchRepository,
			dataStoreUseCases = dataStoreUseCases,
			trinketRepository = trinketRepository,
			syncFavoritesUseCase = syncFavoritesUseCase,
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `refetchAllData_should_return_Success_when_all_categories_succeed`() = runTest {
		// Given
		val language = "en"
		mockLanguageState(language)

		// Mock all repositories to return successful responses with data
		mockSuccessfulResponses(language)

		// When
		val result = useCase.refetchAllData(forceRefresh = true)

		// Then
		assertTrue(result is DataRefetchResult.Success)
	}

	@Test
	fun `refetchAllData_should_return_PartialSuccess_when_some_categories_fail`() = runTest {
		// Given
		val language = "en"
		mockLanguageState(language)

		// Mock some successful and some failed responses
		mockPartialSuccessfulResponses(language)

		// When
		val result = useCase.refetchAllData(forceRefresh = true)

		// Then
		assertTrue(result is DataRefetchResult.PartialSuccess)
		val partialSuccess = result as DataRefetchResult.PartialSuccess
		assertTrue(partialSuccess.successfulCategories.isNotEmpty())
		assertTrue(partialSuccess.failedCategories.isNotEmpty())
		assertEquals(15, partialSuccess.totalCategories)
	}

	@Test
	fun `refetchAllData_should_return_NetworkError_when_all_categories_fail`() = runTest {
		// Given
		val language = "en"
		mockLanguageState(language)

		// Mock all repositories to return failed responses
		mockFailedResponses(language)

		// When
		val result = useCase.refetchAllData(forceRefresh = true)

		// Then
		assertTrue(result is DataRefetchResult.NetworkError)
	}

	private suspend fun mockLanguageState(language: String, cachedLanguage: String = language) {
		val cacheToken = "$language:translations-v2"
		val cachedDataMarker = if (cachedLanguage == language) cacheToken else cachedLanguage
		whenever(languageProvider()).thenReturn(flowOf(language))
		whenever(dataLanguageProvider()).thenReturn(flowOf(cachedDataMarker))
		whenever(saveDataLanguageState(cacheToken)).thenReturn(Unit)
	}

	private suspend fun mockSuccessfulResponses(language: String) {
		// Mock successful responses for all repositories
		whenever(biomeRepository.fetchBiomes(language)).thenReturn(Response.success(listOf(sampleBiome())))
		whenever(creatureRepository.fetchCreatures(language)).thenReturn(Response.success(listOf(sampleCreature())))
		whenever(oreDepositRepository.fetchOreDeposits(language)).thenReturn(
			Response.success(
				listOf(sampleOreDeposit())
			)
		)
		whenever(materialsRepository.fetchMaterials(language)).thenReturn(Response.success(listOf(sampleMaterial())))
		whenever(pointOfInterestRepository.fetchPointOfInterests(language)).thenReturn(
			Response.success(
				listOf(samplePointOfInterest())
			)
		)
		whenever(treeRepository.fetchTrees(language)).thenReturn(Response.success(listOf(sampleTree())))
		whenever(foodRepository.fetchFoodList(language)).thenReturn(Response.success(listOf(sampleFood())))
		whenever(weaponRepository.fetchWeapons(language)).thenReturn(Response.success(listOf(sampleWeapon())))
		whenever(armorRepository.fetchArmor(language)).thenReturn(Response.success(listOf(sampleArmor())))
		whenever(meadRepository.fetchMeads(language)).thenReturn(Response.success(listOf(sampleMead())))
		whenever(toolRepository.fetchTools(language)).thenReturn(Response.success(listOf(sampleTool())))
		whenever(trinketRepository.fetchTrinkets(language)).thenReturn(Response.success(listOf(sampleTrinket())))
		whenever(buildingMaterialRepository.fetchBuildingMaterial(language)).thenReturn(
			Response.success(
				listOf(sampleBuildingMaterial())
			)
		)
		whenever(craftingObjectRepository.fetchCraftingObject(language)).thenReturn(
			Response.success(
				listOf(sampleCraftingObject())
			)
		)
		whenever(relationsRepository.fetchRelations()).thenReturn(Response.success(listOf(sampleRelation())))
	}

	private suspend fun mockPartialSuccessfulResponses(language: String) {
		// Mock some successful and some failed responses
		whenever(biomeRepository.fetchBiomes(language)).thenReturn(Response.success(listOf(sampleBiome())))
		whenever(creatureRepository.fetchCreatures(language)).thenReturn(Response.success(listOf(sampleCreature())))
		whenever(oreDepositRepository.fetchOreDeposits(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(materialsRepository.fetchMaterials(language)).thenReturn(Response.success(listOf(sampleMaterial())))
		whenever(pointOfInterestRepository.fetchPointOfInterests(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(treeRepository.fetchTrees(language)).thenReturn(Response.success(listOf(sampleTree())))
		whenever(foodRepository.fetchFoodList(language)).thenReturn(Response.success(listOf(sampleFood())))
		whenever(weaponRepository.fetchWeapons(language)).thenReturn(Response.success(listOf(sampleWeapon())))
		whenever(armorRepository.fetchArmor(language)).thenReturn(Response.success(listOf(sampleArmor())))
		whenever(meadRepository.fetchMeads(language)).thenReturn(Response.success(listOf(sampleMead())))
		whenever(toolRepository.fetchTools(language)).thenReturn(Response.success(listOf(sampleTool())))
		whenever(trinketRepository.fetchTrinkets(language)).thenReturn(Response.success(listOf(sampleTrinket())))
		whenever(buildingMaterialRepository.fetchBuildingMaterial(language)).thenReturn(
			Response.success(
				listOf(sampleBuildingMaterial())
			)
		)
		whenever(craftingObjectRepository.fetchCraftingObject(language)).thenReturn(
			Response.success(
				listOf(sampleCraftingObject())
			)
		)
		whenever(relationsRepository.fetchRelations()).thenReturn(Response.success(listOf(sampleRelation())))
	}

	private suspend fun mockFailedResponses(language: String) {
		// Mock failed responses for all repositories
		whenever(biomeRepository.fetchBiomes(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(creatureRepository.fetchCreatures(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(oreDepositRepository.fetchOreDeposits(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(materialsRepository.fetchMaterials(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(pointOfInterestRepository.fetchPointOfInterests(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(treeRepository.fetchTrees(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(foodRepository.fetchFoodList(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(weaponRepository.fetchWeapons(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(armorRepository.fetchArmor(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(meadRepository.fetchMeads(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(toolRepository.fetchTools(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(trinketRepository.fetchTrinkets(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(buildingMaterialRepository.fetchBuildingMaterial(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(craftingObjectRepository.fetchCraftingObject(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(relationsRepository.fetchRelations()).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
	}

	private fun sampleBiome() = Biome(id = "biome", category = "BIOME", imageUrl = "", name = "Biome", description = "", order = 1)
	private fun sampleCreature() = Creature(id = "creature", category = "CREATURE", imageUrl = "", name = "Creature", description = "", order = 1)
	private fun sampleOreDeposit() = OreDeposit(id = "ore", category = "OREDEPOSITE", name = "Ore", description = "", order = 1, imageUrl = "")
	private fun sampleMaterial() = Material(id = "material", imageUrl = "", category = "MATERIAL", subCategory = "", name = "Material", description = "", order = 1)
	private fun samplePointOfInterest() = PointOfInterest(id = "poi", imageUrl = "", category = "POINTOFINTEREST", subCategory = "", name = "Poi", description = "", order = 1)
	private fun sampleTree() = Tree(id = "tree", imageUrl = "", category = "TREE", name = "Tree", description = "", order = 1)
	private fun sampleFood() = Food(id = "food", imageUrl = "", category = "FOOD", subCategory = "", name = "Food", description = "", order = 1)
	private fun sampleWeapon() = Weapon(id = "weapon", imageUrl = "", category = "WEAPON", subCategory = "", name = "Weapon", description = "", order = 1)
	private fun sampleArmor() = Armor(id = "armor", imageUrl = "", category = "ARMOR", subCategory = "", name = "Armor", description = "", order = 1)
	private fun sampleMead() = Mead(id = "mead", imageUrl = "", category = "MEAD", subCategory = "", name = "Mead", description = "", order = 1)
	private fun sampleTool() = ItemTool(id = "tool", imageUrl = "", category = "TOOL", subCategory = "", name = "Tool", description = "", order = 1)
	private fun sampleTrinket() = Trinket(id = "trinket", imageUrl = "", category = "TRINKETS", subCategory = "", name = "Trinket", description = "", order = 1)
	private fun sampleBuildingMaterial() = BuildingMaterial(id = "building", imageUrl = "", category = "BUILDING_MATERIAL", subCategory = "", name = "Building", description = "", order = 1)
	private fun sampleCraftingObject() = CraftingObject(id = "crafting", imageUrl = "", category = "CRAFTING", subCategory = "", name = "Crafting", description = "", order = 1)
	private fun sampleRelation() = Relation(id = "relation", mainItemId = "a", relatedItemId = "b")
}
