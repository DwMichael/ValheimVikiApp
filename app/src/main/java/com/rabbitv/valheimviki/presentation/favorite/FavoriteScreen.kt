package com.rabbitv.valheimviki.presentation.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.FavoriteGridItem
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiEvent
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiState
import com.rabbitv.valheimviki.presentation.favorite.viewmodel.FavoriteViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_SMALL_IMAGES
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.toAppCategory
import kotlinx.coroutines.launch

class FavoriteChip(
	override val option: AppCategory,
	override val icon: ImageVector,
	override val label: String
) : ChipData<AppCategory>

enum class FavoriteGridItemTypes {
	SMALL, MEDIUM, DEFAULT
}

@Composable
fun FavoriteScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: FavoriteViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onCategorySelected = { category: AppCategory? ->
		viewModel.onEvent(FavoriteUiEvent.CategorySelected(category))
	}
	FavoriteScreenContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onCategorySelected = onCategorySelected,
		uiState = uiState,
	)

}


@Composable
fun FavoriteScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onCategorySelected: (category: AppCategory?) -> Unit,
	uiState: FavoriteUiState
) {
	val lazyGridState = rememberLazyStaggeredGridState()
	val scope = rememberCoroutineScope()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyGridState.firstVisibleItemIndex >= 2 }
	}
	val painter = painterResource(R.drawable.bg_favorite_item_grid)
	val imageBg = remember {
		@Composable {
			Image(
				painter = painter,
				contentDescription = "bg",
				contentScale = ContentScale.FillBounds,
				modifier = Modifier
					.clip(Shapes.large)
					.fillMaxSize()
			)
		}
	}

	val handleFavoriteItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	val typeOfItemGrid = remember {
		{ appCategory: AppCategory ->
			determineFavoriteGridType(appCategory)
		}
	}

	val chips = remember { getChipsForCategory() }
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
			.testTag("FavoriteGridScaffold"),
		floatingActionButton = {
			CustomFloatingActionButton(
				showBackButton = backButtonVisibleState,
				onClick = {
					scope.launch {
						lazyGridState.animateScrollToItem(0)
					}
				},
				bottomPadding = 0.dp
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		content = { innerScaffoldPadding ->
			LazyVerticalStaggeredGrid(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerScaffoldPadding)
					.padding(BODY_CONTENT_PADDING.dp),
				state = lazyGridState,
				columns = StaggeredGridCells.Fixed(2),
				horizontalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
				verticalItemSpacing = BODY_CONTENT_PADDING.dp,
				contentPadding = PaddingValues(bottom = 100.dp),
			) {
				item(span = StaggeredGridItemSpan.FullLine, key = "SlavicDivider") {
					SlavicDivider()
				}
				item(span = StaggeredGridItemSpan.FullLine) {
					SearchFilterBar(
						chips = chips,
						selectedOption = uiState.selectedCategory,
						onSelectedChange = { _, category -> onCategorySelected(category) },
						modifier = Modifier,
					)
					Spacer(
						Modifier.padding(
							horizontal = BODY_CONTENT_PADDING.dp,
							vertical = 5.dp
						)
					)
				}
				when (val state = uiState.favoritesState) {
					is UIState.Loading -> null
					is UIState.Error -> null
					is UIState.Success -> {

						item(span = StaggeredGridItemSpan.FullLine, key = "SlavicDivider2") {
							SlavicDivider()
						}
						items(
							items = state.data,
							key = { favorite -> favorite.id },
							contentType = { favorite -> favorite.category }
						) { favorite ->
							when (typeOfItemGrid(favorite.category.toAppCategory())) {
								FavoriteGridItemTypes.SMALL -> FavoriteGridItem(
									imageModifier = Modifier
										.fillMaxSize()
										.scale(0.7f),
									item = favorite,
									onItemClick = handleFavoriteItemClick,
									height = ITEM_HEIGHT_SMALL_IMAGES,
									contentScale = ContentScale.Fit,
									imageBg = imageBg
								)

								FavoriteGridItemTypes.MEDIUM -> FavoriteGridItem(
									item = favorite,
									onItemClick = handleFavoriteItemClick,
									height = ITEM_HEIGHT_TWO_COLUMNS,
									contentScale = ContentScale.Fit,
									imageBg = imageBg
								)

								FavoriteGridItemTypes.DEFAULT -> FavoriteGridItem(
									item = favorite,
									onItemClick = handleFavoriteItemClick,
									height = ITEM_HEIGHT_TWO_COLUMNS,
								)
							}
						}
					}
				}
			}
		}
	)
}

private fun determineFavoriteGridType(appCategory: AppCategory): FavoriteGridItemTypes {

	return when (appCategory) {
		AppCategory.BIOME -> FavoriteGridItemTypes.DEFAULT
		AppCategory.CREATURE -> FavoriteGridItemTypes.DEFAULT
		AppCategory.FOOD -> FavoriteGridItemTypes.SMALL
		AppCategory.ARMOR -> FavoriteGridItemTypes.SMALL
		AppCategory.WEAPON -> FavoriteGridItemTypes.SMALL
		AppCategory.BUILDING_MATERIAL -> FavoriteGridItemTypes.SMALL
		AppCategory.MATERIAL -> FavoriteGridItemTypes.SMALL
		AppCategory.CRAFTING -> FavoriteGridItemTypes.MEDIUM
		AppCategory.TOOL -> FavoriteGridItemTypes.SMALL
		AppCategory.MEAD -> FavoriteGridItemTypes.SMALL
		AppCategory.POINTOFINTEREST -> FavoriteGridItemTypes.DEFAULT
		AppCategory.TREE -> FavoriteGridItemTypes.DEFAULT
		AppCategory.OREDEPOSITE -> FavoriteGridItemTypes.DEFAULT
		AppCategory.TRINKETS -> FavoriteGridItemTypes.SMALL
	}
}

private fun getChipsForCategory(): List<FavoriteChip> {
	return listOf(
		FavoriteChip(
			option = AppCategory.BIOME,
			icon = Lucide.MountainSnow,
			label = "Biomes"
		),
		FavoriteChip(
			option = AppCategory.CREATURE,
			icon = Lucide.Rabbit,
			label = "Creatures"
		),
		FavoriteChip(
			option = AppCategory.FOOD,
			icon = Lucide.Utensils,
			label = "Food"
		),
		FavoriteChip(
			option = AppCategory.ARMOR,
			icon = Lucide.Shield,
			label = "Armor"
		),
		FavoriteChip(
			option = AppCategory.WEAPON,
			icon = Lucide.Swords,
			label = "Weapons"
		),
		FavoriteChip(
			option = AppCategory.BUILDING_MATERIAL,
			icon = Lucide.House,
			label = "Building Materials"
		),
		FavoriteChip(
			option = AppCategory.MATERIAL,
			icon = Lucide.Cuboid,
			label = "Materials"
		),
		FavoriteChip(
			option = AppCategory.CRAFTING,
			icon = Lucide.Anvil,
			label = "Crafting Stations"
		),
		FavoriteChip(
			option = AppCategory.TOOL,
			icon = Lucide.Gavel,
			label = "Tools"
		),
		FavoriteChip(
			option = AppCategory.MEAD,
			icon = Lucide.FlaskRound,
			label = "Meads"
		),
		FavoriteChip(
			option = AppCategory.POINTOFINTEREST,
			icon = Lucide.MapPinned,
			label = "Points Of Interest"
		),
		FavoriteChip(
			option = AppCategory.TREE,
			icon = Lucide.Trees,
			label = "Trees"
		),
		FavoriteChip(
			option = AppCategory.OREDEPOSITE,
			icon = Lucide.Pickaxe,
			label = "Ore Deposits"
		)
	)
}


@Preview("FavoriteScreenContent - Loading", showBackground = true)
@Composable
fun PreviewFavoriteScreenContentLoading() {
	ValheimVikiAppTheme {
		FavoriteScreenContent(
			onBack = {},
			onItemClick = {},
			onCategorySelected = {},
			uiState = FavoriteUiState()
		)
	}
}

