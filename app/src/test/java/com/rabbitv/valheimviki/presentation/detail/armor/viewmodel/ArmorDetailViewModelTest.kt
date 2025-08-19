package com.rabbitv.valheimviki.presentation.detail.armor.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id.GetArmorByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_ids.GetCraftingObjectByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.presentation.detail.armor.model.ArmorDetailUiEvent
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
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class ArmorDetailViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	private val testArmor = Armor(
		id = "armor1",
		category = AppCategory.ARMOR.toString(),
		subCategory = "",
		imageUrl = "test_image.png",
		name = "Test Armor",
		description = "Test armor description",
		order = 1
	)
	private val testMaterial = Material(
		id = "m1", category = AppCategory.MATERIAL.toString(), subCategory = "",
		imageUrl = "", name = "M1", description = "", order = 2
	)

	private val craftingObject = CraftingObject(
		id = "cO1", category = AppCategory.CRAFTING.toString(), subCategory = "",
		imageUrl = "", name = "CO1", description = "", order = 1
	)


	private val testRelations = listOf(
		RelatedItem(
			id = "armor1", quantity = 1, relationType = "r1"
		),
		RelatedItem(
			id = "m1", quantity = 1, relationType = "r2",
			quantity2star = 2,
			quantity3star = 3,
			quantity4star = 4
		),
		RelatedItem(
			id = "cO1", quantity = 1, relationType = "r3"
		),
		RelatedItem(id = "cO2", quantity = 1, relationType = "r4"),
	)

	@Mock
	private lateinit var armorUseCases: ArmorUseCases

	@Mock
	private lateinit var getArmorByIdUseCase: GetArmorByIdUseCase

	@Mock
	private lateinit var relationsUseCases: RelationUseCases

	@Mock
	lateinit var getRelatedIdsRelationUseCase: GetRelatedIdsRelationUseCase

	@Mock
	private lateinit var materialUseCases: MaterialUseCases

	@Mock
	lateinit var getMaterialsByIds: GetMaterialsByIdsUseCase

	@Mock
	private lateinit var craftingObjectUseCases: CraftingObjectUseCases

	@Mock
	private lateinit var getCraftingObjectByIds: GetCraftingObjectByIdsUseCase

	@Mock
	private lateinit var favoriteUseCases: FavoriteUseCases

	private lateinit var savedStateHandle: SavedStateHandle

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		savedStateHandle = SavedStateHandle(
			mapOf(
				"armorId" to testArmor.id
			)
		)

		whenever(armorUseCases.getArmorByIdUseCase).thenReturn(getArmorByIdUseCase)

		favoriteUseCases = FavoriteUseCases(
			isFavorite = mock(),
			getAllFavoritesUseCase = mock(),
			deleteFavoriteUseCase = mock(),
			addFavoriteUseCase = mock(),
			toggleFavoriteUseCase = mock(),
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	private fun armorViewModel() =
		ArmorDetailViewModel(
			savedStateHandle = savedStateHandle,
			relationsUseCases = relationsUseCases,
			materialUseCases = materialUseCases,
			favoriteUseCases = favoriteUseCases,
			armorUseCases = armorUseCases,
			craftingObjectUseCases = craftingObjectUseCases,
			defaultDispatcher = testDispatcher
		)

	private fun emitValuesFromUseCases(
		armor: Armor = testArmor,
		relations: List<RelatedItem> = testRelations,
		crafting: CraftingObject? = null,
		materials: List<Material> = emptyList(),
		isFav: Boolean = false,
	) {
		whenever(armorUseCases.getArmorByIdUseCase).thenReturn(getArmorByIdUseCase)
		whenever(getArmorByIdUseCase(any())).thenReturn(flowOf(armor))

		whenever(relationsUseCases.getRelatedIdsUseCase).thenReturn(getRelatedIdsRelationUseCase)
		whenever(getRelatedIdsRelationUseCase(any())).thenReturn(flowOf(relations))

		whenever(materialUseCases.getMaterialsByIds).thenReturn(getMaterialsByIds)
		whenever(getMaterialsByIds(any())).thenReturn(flowOf(materials))

		whenever(craftingObjectUseCases.getCraftingObjectByIds).thenReturn(getCraftingObjectByIds)
		whenever(getCraftingObjectByIds(any())).thenReturn(flowOf(crafting))

		whenever(favoriteUseCases.isFavorite(any())).thenReturn(flowOf(isFav))
	}


	@Test
	fun initial_EmissionContentEmpty_ShouldEmitInitValues() = runTest {
		emitValuesFromUseCases()

		val vm = armorViewModel()

		vm.uiState.test {
			awaitItem()
			awaitItem()
			val state = awaitItem()

			assertEquals(testArmor.id, state.armor!!.id)
			assertTrue(state.craftingObject is UIState.Success)
			assertNull((state.craftingObject).data)
			assertTrue((state.materials as UIState.Success).data.isEmpty())

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun uiState_DataFetched_ShouldEmitSuccessWithData() = runTest {

		val materials = listOf(
			testMaterial.copy(id = "m5"),
			testMaterial,
			testMaterial.copy(id = "m2"),
			testMaterial.copy(id = "m3")
		)
		val relations = listOf(
			RelatedItem(
				id = "m5",
				relationType = "r2",
				quantity = 1,
				quantity2star = 2,
				quantity3star = 3,
				quantity4star = 4
			),
			RelatedItem(
				id = "m1",
				relationType = "r2",
				quantity = 0,
				quantity2star = 0,
				quantity3star = 0,
				quantity4star = 0
			),
			RelatedItem(
				id = "m2",
				relationType = "r2",
				quantity = 0,
				quantity2star = 0,
				quantity3star = 0,
				quantity4star = 0
			),
			RelatedItem(
				id = "m3",
				relationType = "r2",
				quantity = 0,
				quantity2star = 0,
				quantity3star = 0,
				quantity4star = 0
			),
		)



		emitValuesFromUseCases(
			relations = relations,
			crafting = craftingObject,
			materials = materials,
			isFav = false
		)


		val vm = armorViewModel()

		vm.uiState.test {
			skipItems(2)
			val state = awaitItem()

			assertEquals(testArmor, state.armor)
			when (val state = state.craftingObject) {
				is UIState.Error -> fail("Should not emit error craftingObject")
				is UIState.Loading -> fail("data is fetched so success should be emit not loading craftingObject")
				is UIState.Success -> {
					assertEquals(craftingObject, state.data)
				}
			}
			when (val state = state.materials) {
				is UIState.Error -> fail("Should not emit error materials")
				is UIState.Loading -> fail("data is fetched so success should be emit not loading materials")
				is UIState.Success<List<MaterialUpgrade>> -> {

					val upgrades = state.data
					val m5Upgrade = upgrades.first { it.material.id == "m5" }
					assertEquals(listOf(1, 2, 3, 4), m5Upgrade.quantityList)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun uiEvent_NotSelected_ShouldAddToFavorite() = runTest {
		emitValuesFromUseCases(isFav = false)
		val vm = armorViewModel()

		val favoriteArmor = Favorite(
			id = testArmor.id,
			name = testArmor.name,
			description = testArmor.description,
			imageUrl = testArmor.imageUrl,
			category = testArmor.category,
			subCategory = testArmor.subCategory,
		)

		vm.uiState.test {
			skipItems(2)
			val successEmit = awaitItem()
			assertFalse(successEmit.isFavorite)

			vm.uiEvent(ArmorDetailUiEvent.ToggleFavorite(favoriteArmor))
			advanceUntilIdle()

			val resultEmit = awaitItem()
			assertTrue(resultEmit.isFavorite)

		}
	}

	@Test
	fun uiEvent_SelectedFavorite_ShouldDeletedFromFavorite() = runTest {

		emitValuesFromUseCases(isFav = true)
		val vm = armorViewModel()

		val favoriteArmor = Favorite(
			id = testArmor.id,
			name = testArmor.name,
			description = testArmor.description,
			imageUrl = testArmor.imageUrl,
			category = testArmor.category,
			subCategory = testArmor.subCategory,
		)

		vm.uiState.test {
			skipItems(2)
			val successEmit = awaitItem()
			assertTrue(successEmit.isFavorite)

			vm.uiEvent(ArmorDetailUiEvent.ToggleFavorite(favoriteArmor))
			advanceUntilIdle()

			val resultEmit = awaitItem()
			assertFalse(resultEmit.isFavorite)


		}
	}
}