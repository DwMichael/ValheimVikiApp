package com.rabbitv.valheimviki.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Anvil
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Utensils
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
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.navigation.TopLevelDestination
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerRowEffect
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.favorite.model.UiStateFavorite
import com.rabbitv.valheimviki.presentation.favorite.viewmodel.FavoriteViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.launch


@Composable
fun FavoriteScreen(
	onBack: () -> Unit,
	onItemClick: (destination: TopLevelDestination) -> Unit,
	viewModel: FavoriteViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FavoriteScreenContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
	)

}


@Composable
fun FavoriteScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: TopLevelDestination) -> Unit,
	uiState: UiStateFavorite
) {
	val lazyListState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}

	val maxHeightFirst = 380.dp
	val pageWidthFirst = 250.dp
	val itemHeightFirst = 180.dp
	val itemWidthFirst = 400.dp

	val maxHeightSecond = 300.dp
	val pageWidthSecond = 220.dp
	val itemHeightSecond = 180.dp
	val itemWidthSecond = 200.dp

	val biomeData = HorizontalPagerData(
		title = "Biomes",
		subTitle = "",
		icon = Lucide.MountainSnow,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "",
		icon = Lucide.Rabbit,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val weaponData = HorizontalPagerData(
		title = "Weapons",
		subTitle = "",
		icon = Lucide.Swords,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val armorData = HorizontalPagerData(
		title = "Armor",
		subTitle = "",
		icon = Lucide.Shield,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val foodData = HorizontalPagerData(
		title = "Food",
		subTitle = "",
		icon = Lucide.Utensils,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val meadData = HorizontalPagerData(
		title = "Maeds",
		subTitle = "",
		icon = Lucide.FlaskRound,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val craftingObjectData = HorizontalPagerData(
		title = "Crafting Stations",
		subTitle = "",
		icon = Lucide.Anvil,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val toolData = HorizontalPagerData(
		title = "Tools",
		subTitle = "",
		icon = Lucide.Gavel,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val materialData = HorizontalPagerData(
		title = "Materials",
		subTitle = "",
		icon = Lucide.Cuboid,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val buildingMaterialData = HorizontalPagerData(
		title = "Building Materials",
		subTitle = "",
		icon = Lucide.House,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val oreDepositData = HorizontalPagerData(
		title = "Ore Deposits",
		subTitle = "",
		icon = Lucide.Pickaxe,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val treeData = HorizontalPagerData(
		title = "Trees",
		subTitle = "",
		icon = Lucide.Trees,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val pointOfInterestData = HorizontalPagerData(
		title = "Points Of Interest",
		subTitle = "",
		icon = Lucide.MapPinned,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)



	Scaffold(
		topBar = {
			SimpleTopBar(
				modifier = Modifier,
				title = "Favorites",
				onClick = {
					onBack()
				}
			)
		},
		modifier = Modifier
			.testTag("MaterialListScaffold"),
		floatingActionButton = {
			CustomFloatingActionButton(
				showBackButton = backButtonVisibleState,
				onClick = {
					scope.launch {
						lazyListState.animateScrollToItem(0)
					}
				},
				bottomPadding = 0.dp
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		content = { innerScaffoldPadding ->
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerScaffoldPadding)
					.padding(BODY_CONTENT_PADDING.dp),
				state = lazyListState,
				horizontalAlignment = Alignment.Start,
				verticalArrangement = Arrangement.Top
			) {
				item {
					SlavicDivider()
				}
				item {
					when (val state = uiState.biomes) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Biome>> ->
							if (state.data.isNotEmpty()) {
								HorizontalPagerSection(
									list = state.data,
									data = biomeData,
									onItemClick = {},
									maxHeight = maxHeightFirst,
									pageWidth = pageWidthFirst,
									itemHeight = itemHeightFirst,
									itemWidth = itemWidthFirst
								)
							}
					}
				}
				item {
					when (val state = uiState.creatures) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Creature>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = creatureData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemHeight = itemHeightSecond,
									itemWidth = itemWidthSecond
								)
							}
					}
				}
				item {
					when (val state = uiState.weapons) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Weapon>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = weaponData,
									onItemClick = {},
								)
							}
					}
				}
				item {
					when (val state = uiState.armors) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Armor>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = armorData,
									onItemClick = {},
								)
							}
					}
				}
				item {
					when (val state = uiState.foods) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Food>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = foodData,
									onItemClick = {},
								)
							}
					}
				}
				item {
					when (val state = uiState.meads) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Mead>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = meadData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemHeight = itemHeightSecond,
									itemWidth = itemWidthSecond
								)
							}
					}
				}
				item {
					when (val state = uiState.craftingObjects) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<CraftingObject>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = craftingObjectData,
									onItemClick = {},
									maxHeight = maxHeightFirst,
									pageWidth = pageWidthFirst,
									itemHeight = itemHeightFirst,
									itemWidth = itemWidthFirst
								)
							}
					}
				}
				item {
					when (val state = uiState.tools) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<ItemTool>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = toolData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemHeight = itemHeightSecond,
									itemWidth = itemWidthSecond
								)
							}
					}
				}
				item {
					when (val state = uiState.materials) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Material>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = materialData,
									onItemClick = {},
								)
							}
					}
				}
				item {
					when (val state = uiState.buildingMaterials) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<BuildingMaterial>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = buildingMaterialData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemHeight = itemHeightSecond,
									itemWidth = itemWidthSecond
								)
							}
					}
				}
				item {
					when (val state = uiState.oreDeposits) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<OreDeposit>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = oreDepositData,
									onItemClick = {},
									maxHeight = maxHeightFirst,
									pageWidth = pageWidthFirst,
									itemHeight = itemHeightFirst,
									itemWidth = itemWidthFirst
								)
							}
					}
				}
				item {
					when (val state = uiState.trees) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Tree>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = treeData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemHeight = itemHeightSecond,
									itemWidth = itemWidthSecond
								)
							}
					}
				}
				item {
					when (val state = uiState.pointsOfInterest) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<PointOfInterest>> ->
							if (state.data.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = state.data,
									data = pointOfInterestData,
									onItemClick = {},
									maxHeight = maxHeightFirst,
									pageWidth = pageWidthFirst,
									itemHeight = itemHeightFirst,
									itemWidth = itemWidthFirst
								)
							}
					}
				}
				
			}
		}
	)
}


@Preview("FavoriteScreenContent", showBackground = true)
@Composable
fun PreviewFavoriteScreenContent() {
	ValheimVikiAppTheme {
		FavoriteScreenContent(
			onBack = {},
			onItemClick = {},
			uiState = UiStateFavorite(
				creatures = UiState.Success(FakeData.generateFakeCreatures()),
				weapons = UiState.Success(FakeData.fakeWeaponList),
				materials = UiState.Success(FakeData.generateFakeMaterials())
			)
		)
	}
}