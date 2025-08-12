package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category.GetCreatureByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.add_to_favorite.AddFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.delete_from_favorite.DeleteFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.is_favorite.IsFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids.GetPointsOfInterestByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
@Config(sdk = [34])
class BiomeDetailScreenViewModelTest {

	private val testDispatcher = StandardTestDispatcher()


	private val testBiome = Biome(
		id = "biome1",
		category = AppCategory.BIOME.toString(),
		subCategory = "",
		imageUrl = "test_image.png",
		name = "Test Biome",
		description = "Test biome description",
		order = 1
	)

	private val bossCreature = Creature(
		id = "boss1",
		category = AppCategory.CREATURE.toString(),
		subCategory = CreatureSubCategory.BOSS.toString(),
		imageUrl = "boss.png",
		name = "Test Boss",
		description = "Test boss",
		order = 1
	)

	private val testMainBoss = MainBoss(
		id = "boss1",
		category = AppCategory.CREATURE.toString(),
		subCategory = CreatureSubCategory.BOSS.toString(),
		imageUrl = "boss.png",
		name = "Test Boss",
		description = "Test boss",
		order = 1,
		baseHP = 0,
		weakness = "",
		resistance = "",
		baseDamage = "",
		collapseImmune = "",
		forsakenPower = "",
	)

	private val testCreature = Creature(
		id = "c1",
		category = AppCategory.CREATURE.toString(),
		subCategory = CreatureSubCategory.AGGRESSIVE_CREATURE.toString(),
		imageUrl = "c1.png",
		name = "Creature 1",
		description = "",
		order = 1
	)
	private val testMaterial = Material(
		id = "m1", category = AppCategory.MATERIAL.toString(), subCategory = "",
		imageUrl = "", name = "M1", description = "", order = 2
	)
	private val testPoi = PointOfInterest(
		id = "p1", category = AppCategory.POINTOFINTEREST.toString(), subCategory = "",
		imageUrl = "", name = "P1", description = "", order = 1
	)
	private val testTree = Tree(
		id = "t1", category = AppCategory.TREE.toString(), subCategory = "",
		imageUrl = "", name = "T1", description = "", order = 1
	)
	private val ore0 = OreDeposit(
		id = "o0", category = AppCategory.OREDEPOSITE.toString(), subCategory = "",
		imageUrl = "", name = "O0", description = "", order = 1
	)
	private val ore1 = OreDeposit(
		id = "o1", category = AppCategory.OREDEPOSITE.toString(), subCategory = "",
		imageUrl = "", name = "O1", description = "", order = 2
	)

	private val testRelations = listOf(
		RelatedItem(id = "c1", quantity = 1, relationType = "r1"),
		RelatedItem(id = "m1", quantity = 1, relationType = "r2"),
		RelatedItem(id = "boss1", quantity = 1, relationType = "boss_of"),
		RelatedItem(id = "o1", quantity = 1, relationType = "r3"),
		RelatedItem(id = "o0", quantity = 1, relationType = "r3"),
		RelatedItem(id = "p1", quantity = 1, relationType = "r4"),
		RelatedItem(id = "t1", quantity = 1, relationType = "r5"),
	)

	// --- Mocks
	@Mock
	lateinit var biomeUseCases: BiomeUseCases

	@Mock
	lateinit var getBiomeByIdUseCase: GetBiomeByIdUseCase

	@Mock
	lateinit var creaturesUseCase: CreatureUseCases

	@Mock
	lateinit var getCreaturesByIds: GetCreaturesByIdsUseCase

	@Mock
	lateinit var getCreatureByRelationAndSubCategory: GetCreatureByRelationAndSubCategory

	@Mock
	lateinit var relationsUseCases: RelationUseCases

	@Mock
	lateinit var getRelatedIdsRelationUseCase: GetRelatedIdsRelationUseCase

	@Mock
	lateinit var materialUseCases: MaterialUseCases

	@Mock
	lateinit var getMaterialsByIds: GetMaterialsByIdsUseCase

	@Mock
	lateinit var pointOfInterestUseCases: PointOfInterestUseCases

	@Mock
	lateinit var getPointsOfInterestByIdsUseCase: GetPointsOfInterestByIdsUseCase

	@Mock
	lateinit var treeUseCases: TreeUseCases

	@Mock
	lateinit var getTreesByIdsUseCase: GetTreesByIdsUseCase

	@Mock
	lateinit var oreDepositUseCases: OreDepositUseCases

	@Mock
	lateinit var getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase

	@Mock
	lateinit var favoriteUseCases: FavoriteUseCases

	@Mock
	lateinit var isFavorite: IsFavoriteUseCase

	@Mock
	lateinit var addFavoriteUseCase: AddFavoriteUseCase

	@Mock
	lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

	private lateinit var savedStateHandle: SavedStateHandle

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)


		savedStateHandle = SavedStateHandle(
			mapOf(
				"biomeId" to testBiome.id,
				"imageUrl" to testBiome.imageUrl,
				"title" to testBiome.name
			)
		)

		whenever(biomeUseCases.getBiomeByIdUseCase).thenReturn(getBiomeByIdUseCase)


	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	private fun createViewModel() =
		BiomeDetailScreenViewModel(
			savedStateHandle = savedStateHandle,
			biomeUseCases = biomeUseCases,
			creaturesUseCase = creaturesUseCase,
			relationsUseCases = relationsUseCases,
			materialUseCases = materialUseCases,
			pointOfInterestUseCases = pointOfInterestUseCases,
			treeUseCases = treeUseCases,
			oreDepositUseCases = oreDepositUseCases,
			favoriteUseCases = favoriteUseCases,
			defaultDispatcher = testDispatcher
		)


	private fun linkContentUseCases() {
		whenever(creaturesUseCase.getCreaturesByIds).thenReturn(getCreaturesByIds)
		whenever(materialUseCases.getMaterialsByIds).thenReturn(getMaterialsByIds)
		whenever(pointOfInterestUseCases.getPointsOfInterestByIdsUseCase).thenReturn(
			getPointsOfInterestByIdsUseCase
		)
		whenever(treeUseCases.getTreesByIdsUseCase).thenReturn(getTreesByIdsUseCase)
		whenever(oreDepositUseCases.getOreDepositsByIdsUseCase).thenReturn(
			getOreDepositsByIdsUseCase
		)
	}


	private fun emitValuesFromUseCases(
		biome: Biome = testBiome,
		relations: List<RelatedItem> = testRelations,
		creature: Creature? = null,
		isFavorite: Boolean = false,
		creatures: List<Creature>? = null,
		materials: List<Material>? = null,
		pointsOfInterest: List<PointOfInterest>? = null,
		trees: List<Tree>? = null,
		oreDeposits: List<OreDeposit>? = null,
	) {
		whenever(creaturesUseCase.getCreatureByRelationAndSubCategory).thenReturn(
			getCreatureByRelationAndSubCategory
		)
		whenever(relationsUseCases.getRelatedIdsUseCase).thenReturn(getRelatedIdsRelationUseCase)
		whenever(getBiomeByIdUseCase(any())).thenReturn(flowOf(biome))
		whenever(getRelatedIdsRelationUseCase(any())).thenReturn(flowOf(relations))
		whenever(getCreatureByRelationAndSubCategory(any(), any())).thenReturn(flowOf(creature))
		whenever(isFavorite(any())).thenReturn(flowOf(isFavorite))

		if (creatures != null || materials != null || pointsOfInterest != null || trees != null || oreDeposits != null) {
			linkContentUseCases()
		}

		creatures?.let { whenever(getCreaturesByIds(any())).thenReturn(flowOf(it)) }
		materials?.let { whenever(getMaterialsByIds(any())).thenReturn(flowOf(it)) }
		pointsOfInterest?.let {
			whenever(getPointsOfInterestByIdsUseCase(any())).thenReturn(
				flowOf(
					it
				)
			)
		}
		trees?.let { whenever(getTreesByIdsUseCase(any())).thenReturn(flowOf(it)) }
		oreDeposits?.let { whenever(getOreDepositsByIdsUseCase(any())).thenReturn(flowOf(it)) }
	}


	@Test
	fun initial_emission_head_ready_content_empty_after_start() = runTest {
		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)
		emitValuesFromUseCases(
			creatures = emptyList(),
			materials = emptyList(),
			pointsOfInterest = emptyList(),
			trees = emptyList(),
			oreDeposits = emptyList()
		)

		val vm = createViewModel()

		vm.biomeUiState.test {
			awaitItem()      // initial
			awaitItem()      // head-ready
			vm.startContent()
			val state = awaitItem()

			assertEquals(testBiome.id, state.biome!!.id)
			assertTrue(state.mainBoss is UIState.Success)
			assertNull(state.mainBoss.data)

			assertTrue((state.relatedCreatures as UIState.Success).data.isEmpty())
			assertTrue((state.relatedMaterials as UIState.Success).data.isEmpty())
			assertTrue((state.relatedPointOfInterest as UIState.Success).data.isEmpty())
			assertTrue((state.relatedTrees as UIState.Success).data.isEmpty())
			assertTrue((state.relatedOreDeposits as UIState.Success).data.isEmpty())
		}
	}


	@Test
	fun startContent_lists_fetched_and_sorted() = runTest {
		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)
		emitValuesFromUseCases(
			creatures = listOf(testCreature),
			creature = bossCreature,
			materials = listOf(testMaterial),
			pointsOfInterest = listOf(testPoi),
			trees = listOf(testTree),
			oreDeposits = listOf(ore1, ore0)
		)
		val vm = createViewModel()

		vm.biomeUiState.test {
			awaitItem() // initial
			awaitItem() // HEAD
			vm.startContent()
			val state = awaitItem()

			assertEquals(listOf(testCreature), (state.relatedCreatures as UIState.Success).data)
			assertEquals(listOf(testMaterial), (state.relatedMaterials as UIState.Success).data)
			assertEquals(listOf(testPoi), (state.relatedPointOfInterest as UIState.Success).data)
			assertEquals(listOf(testTree), (state.relatedTrees as UIState.Success).data)
			assertEquals(listOf(ore0, ore1), (state.relatedOreDeposits as UIState.Success).data)
		}
	}

	@Test
	fun mainBoss_emitted_when_useCase_returns_boss() = runTest {
		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)
		emitValuesFromUseCases(creature = bossCreature)

		val vm = createViewModel()

		vm.biomeUiState.test {
			awaitItem()
			val state = awaitItem()
			val boss = (state.mainBoss as UIState.Success).data
			assertNotNull(boss)
			assertEquals(testMainBoss.id, boss.id)
			assertEquals(testMainBoss.name, boss.name)
		}
	}

	@Test
	fun toggleFavorite_adds_favorite_when_currentIsFavorite_is_false() = runTest {

		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)
		whenever(isFavorite(any())).thenReturn(flowOf(false))
		whenever(favoriteUseCases.addFavoriteUseCase).thenReturn(addFavoriteUseCase)

		whenever(biomeUseCases.getBiomeByIdUseCase).thenReturn(getBiomeByIdUseCase)
		whenever(relationsUseCases.getRelatedIdsUseCase).thenReturn(getRelatedIdsRelationUseCase)

		whenever(getBiomeByIdUseCase(any())).thenReturn(flowOf(testBiome))
		whenever(getRelatedIdsRelationUseCase(any())).thenReturn(flowOf(emptyList()))


		val vm = createViewModel()

		val testFavorite = Favorite(
			id = testBiome.id,
			category = testBiome.category,
			imageUrl = testBiome.imageUrl,
			name = testBiome.name,
			description = testBiome.description,
			subCategory = testBiome.subCategory,
		)

		vm.toggleFavorite(testFavorite, currentIsFavorite = false)
		testScheduler.advanceUntilIdle()

		verify(addFavoriteUseCase).invoke(testFavorite)
		verify(deleteFavoriteUseCase, never()).invoke(any())
	}

	@Test
	fun toggleFavorite_deletes_favorite_when_currentIsFavorite_is_true() = runTest {

		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)
		whenever(isFavorite(any())).thenReturn(flowOf(true))
		whenever(favoriteUseCases.deleteFavoriteUseCase).thenReturn(deleteFavoriteUseCase)

		whenever(biomeUseCases.getBiomeByIdUseCase).thenReturn(getBiomeByIdUseCase)
		whenever(relationsUseCases.getRelatedIdsUseCase).thenReturn(getRelatedIdsRelationUseCase)

		whenever(getBiomeByIdUseCase(any())).thenReturn(flowOf(testBiome))
		whenever(getRelatedIdsRelationUseCase(any())).thenReturn(flowOf(emptyList()))

		val vm = createViewModel()
		val testFavorite = Favorite(
			id = testBiome.id,
			category = testBiome.category,
			imageUrl = testBiome.imageUrl,
			name = testBiome.name,
			description = testBiome.description,
			subCategory = testBiome.subCategory,
		)

		vm.toggleFavorite(testFavorite, currentIsFavorite = true)

		testScheduler.advanceUntilIdle()

		verify(deleteFavoriteUseCase).invoke(testFavorite)
		verify(addFavoriteUseCase, never()).invoke(any())
	}


	@Test
	fun startContent_sorts_all_related_lists_by_order_for_each_category() = runTest {
		whenever(favoriteUseCases.isFavorite).thenReturn(isFavorite)

		val creaturesUnsorted = listOf(
			testCreature.copy(id = "c5", name = "Creature 5", order = 5),
			testCreature.copy(id = "c1", name = "Creature 1", order = 1),
			testCreature.copy(id = "c3", name = "Creature 3", order = 3),
		)
		val materialsUnsorted = listOf(
			testMaterial.copy(id = "m2", name = "M2", order = 10),
			testMaterial.copy(id = "m0", name = "M0", order = 0),
			testMaterial.copy(id = "m1", name = "M1", order = 2),
		)
		val poiUnsorted = listOf(
			testPoi.copy(id = "p3", name = "P3", order = 3),
			testPoi.copy(id = "p2", name = "P2", order = 2),
			testPoi.copy(id = "p4", name = "P4", order = 4),
		)
		val treesUnsorted = listOf(
			testTree.copy(id = "t2", name = "T2", order = 2),
			testTree.copy(id = "t1", name = "T1", order = 1),
		)
		val oresUnsorted = listOf(
			ore1.copy(order = 2),
			ore0.copy(order = 1),
		)

		emitValuesFromUseCases(
			creatures = creaturesUnsorted,
			materials = materialsUnsorted,
			pointsOfInterest = poiUnsorted,
			trees = treesUnsorted,
			oreDeposits = oresUnsorted
		)

		val vm = createViewModel()

		vm.biomeUiState.test {
			awaitItem()
			awaitItem()
			vm.startContent()
			val state = awaitItem()

			assertEquals(
				listOf(creaturesUnsorted[1], creaturesUnsorted[2], creaturesUnsorted[0]),
				(state.relatedCreatures as UIState.Success).data
			)
			assertEquals(
				listOf(materialsUnsorted[1], materialsUnsorted[2], materialsUnsorted[0]),
				(state.relatedMaterials as UIState.Success).data
			)
			assertEquals(
				listOf(poiUnsorted[1], poiUnsorted[0], poiUnsorted[2]),
				(state.relatedPointOfInterest as UIState.Success).data
			)
			assertEquals(
				listOf(treesUnsorted[1], treesUnsorted[0]),
				(state.relatedTrees as UIState.Success).data
			)

			assertEquals(
				listOf(ore0, ore1),
				(state.relatedOreDeposits as UIState.Success).data
			)
		}
	}


}