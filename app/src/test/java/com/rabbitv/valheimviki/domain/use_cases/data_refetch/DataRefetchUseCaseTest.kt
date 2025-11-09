package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
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
	private lateinit var buildingMaterialRepository: BuildingMaterialRepository

	@Mock
	private lateinit var craftingObjectRepository: CraftingObjectRepository

	@Mock
	private lateinit var searchRepository: SearchRepository

	@Mock
	private lateinit var dataStoreUseCases: DataStoreUseCases

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
		dataStoreUseCases = mock()
		trinketRepository = mock()

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
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `refetchAllData should return Success when all categories succeed`() = runTest {
		// Given
		val language = "en"
		whenever(dataStoreUseCases.languageProvider()).thenReturn(flowOf(language))

		// Mock all repositories to return successful responses with data
		mockSuccessfulResponses(language)

		// When
		val result = useCase.refetchAllData()

		// Then
		assertTrue(result is DataRefetchResult.Success)
	}

	@Test
	fun `refetchAllData should return PartialSuccess when some categories fail`() = runTest {
		// Given
		val language = "en"
		whenever(dataStoreUseCases.languageProvider()).thenReturn(flowOf(language))

		// Mock some successful and some failed responses
		mockPartialSuccessfulResponses(language)

		// When
		val result = useCase.refetchAllData()

		// Then
		assertTrue(result is DataRefetchResult.PartialSuccess)
		val partialSuccess = result as DataRefetchResult.PartialSuccess
		assertTrue(partialSuccess.successfulCategories.isNotEmpty())
		assertTrue(partialSuccess.failedCategories.isNotEmpty())
		assertEquals(14, partialSuccess.totalCategories)
	}

	@Test
	fun `refetchAllData should return NetworkError when all categories fail`() = runTest {
		// Given
		val language = "en"
		whenever(dataStoreUseCases.languageProvider()).thenReturn(flowOf(language))

		// Mock all repositories to return failed responses
		mockFailedResponses(language)

		// When
		val result = useCase.refetchAllData()

		// Then
		assertTrue(result is DataRefetchResult.NetworkError)
	}

	private suspend fun mockSuccessfulResponses(language: String) {
		// Mock successful responses for all repositories
		whenever(biomeRepository.fetchBiomes(language)).thenReturn(Response.success(emptyList()))
		whenever(creatureRepository.fetchCreatures(language)).thenReturn(Response.success(emptyList()))
		whenever(oreDepositRepository.fetchOreDeposits(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(materialsRepository.fetchMaterials(language)).thenReturn(Response.success(emptyList()))
		whenever(pointOfInterestRepository.fetchPointOfInterests(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(treeRepository.fetchTrees(language)).thenReturn(Response.success(emptyList()))
		whenever(foodRepository.fetchFoodList(language)).thenReturn(Response.success(emptyList()))
		whenever(weaponRepository.fetchWeapons(language)).thenReturn(Response.success(emptyList()))
		whenever(armorRepository.fetchArmor(language)).thenReturn(Response.success(emptyList()))
		whenever(meadRepository.fetchMeads(language)).thenReturn(Response.success(emptyList()))
		whenever(toolRepository.fetchTools(language)).thenReturn(Response.success(emptyList()))
		whenever(buildingMaterialRepository.fetchBuildingMaterial(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(craftingObjectRepository.fetchCraftingObject(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(relationsRepository.fetchRelations()).thenReturn(Response.success(emptyList()))
	}

	private suspend fun mockPartialSuccessfulResponses(language: String) {
		// Mock some successful and some failed responses
		whenever(biomeRepository.fetchBiomes(language)).thenReturn(Response.success(emptyList()))
		whenever(creatureRepository.fetchCreatures(language)).thenReturn(Response.success(emptyList()))
		whenever(oreDepositRepository.fetchOreDeposits(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(materialsRepository.fetchMaterials(language)).thenReturn(Response.success(emptyList()))
		whenever(pointOfInterestRepository.fetchPointOfInterests(language)).thenReturn(
			Response.error(
				500,
				ResponseBody.create(null, "")
			)
		)
		whenever(treeRepository.fetchTrees(language)).thenReturn(Response.success(emptyList()))
		whenever(foodRepository.fetchFoodList(language)).thenReturn(Response.success(emptyList()))
		whenever(weaponRepository.fetchWeapons(language)).thenReturn(Response.success(emptyList()))
		whenever(armorRepository.fetchArmor(language)).thenReturn(Response.success(emptyList()))
		whenever(meadRepository.fetchMeads(language)).thenReturn(Response.success(emptyList()))
		whenever(toolRepository.fetchTools(language)).thenReturn(Response.success(emptyList()))
		whenever(buildingMaterialRepository.fetchBuildingMaterial(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(craftingObjectRepository.fetchCraftingObject(language)).thenReturn(
			Response.success(
				emptyList()
			)
		)
		whenever(relationsRepository.fetchRelations()).thenReturn(Response.success(emptyList()))
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
}
