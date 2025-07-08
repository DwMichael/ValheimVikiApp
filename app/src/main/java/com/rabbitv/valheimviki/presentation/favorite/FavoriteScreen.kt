package com.rabbitv.valheimviki.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.navigation.TopLevelDestination
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.FavoriteGridItem
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteCategory
import com.rabbitv.valheimviki.presentation.favorite.model.UiStateFavorite
import com.rabbitv.valheimviki.presentation.favorite.viewmodel.FavoriteViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch

class FavoriteChip(
	override val option: FavoriteCategory,
	override val icon: ImageVector,
	override val label: String
) : ChipData<FavoriteCategory>

@Composable
fun FavoriteScreen(
	onBack: () -> Unit,
	onItemClick: (destination: TopLevelDestination) -> Unit,
	viewModel: FavoriteViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onCategorySelected = { category: FavoriteCategory? ->
		viewModel.onCategorySelected(category)
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
	onItemClick: (destination: TopLevelDestination) -> Unit,
	onCategorySelected: (category: FavoriteCategory?) -> Unit,
	uiState: UiState<UiStateFavorite>
) {
	val lazyGridState = rememberLazyGridState()
	val scope = rememberCoroutineScope()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyGridState.firstVisibleItemIndex >= 2 }
	}


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
						lazyGridState.animateScrollToItem(0)
					}
				},
				bottomPadding = 0.dp
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		content = { innerScaffoldPadding ->
			LazyVerticalGrid(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerScaffoldPadding)
					.padding(BODY_CONTENT_PADDING.dp),
				state = lazyGridState,
				columns = GridCells.Fixed(2),
				horizontalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
				verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
				contentPadding = PaddingValues(bottom = 100.dp),
			) {
				item(span = { GridItemSpan(2) }, key = "SlavicDivider") {
					SlavicDivider()
				}
				when (uiState) {
					is UiState.Loading -> null
					is UiState.Error -> null
					is UiState.Success<UiStateFavorite> -> {
						item(span = { GridItemSpan(2) }) {
							SearchFilterBar(
								chips = getChipsForCategory(),
								selectedOption = uiState.data.selectedCategory,
								onSelectedChange = { _, category ->
									if (uiState.data.selectedCategory == category) {
										onCategorySelected(null)
									} else {
										onCategorySelected(category)
									}
								},
								modifier = Modifier,
							)
							Spacer(
								Modifier.padding(
									horizontal = BODY_CONTENT_PADDING.dp,
									vertical = 5.dp
								)
							)
						}
						item(span = { GridItemSpan(2) }, key = "SlavicDivider2") {
							SlavicDivider()
						}
						items(uiState.data.favorites, key = { favorite -> favorite.id })
						{ favorite ->
							FavoriteGridItem(
								item = favorite,
								onItemClick = {},
								height = ITEM_HEIGHT_TWO_COLUMNS
							)
						}
					}
				}
			}
		}
	)
}


private fun getChipsForCategory(): List<FavoriteChip> {
	return listOf(
		FavoriteChip(
			option = FavoriteCategory.BIOME,
			icon = Lucide.MountainSnow,
			label = "Biomes"
		),
		FavoriteChip(
			option = FavoriteCategory.CREATURE,
			icon = Lucide.Rabbit,
			label = "Creatures"
		),
		FavoriteChip(
			option = FavoriteCategory.FOOD,
			icon = Lucide.Utensils,
			label = "Food"
		),
		FavoriteChip(
			option = FavoriteCategory.ARMOR,
			icon = Lucide.Shield,
			label = "Armor"
		),
		FavoriteChip(
			option = FavoriteCategory.WEAPON,
			icon = Lucide.Swords,
			label = "Weapons"
		),
		FavoriteChip(
			option = FavoriteCategory.BUILDING_MATERIAL,
			icon = Lucide.House,
			label = "Building Materials"
		),
		FavoriteChip(
			option = FavoriteCategory.MATERIAL,
			icon = Lucide.Cuboid,
			label = "Materials"
		),
		FavoriteChip(
			option = FavoriteCategory.CRAFTING,
			icon = Lucide.Anvil,
			label = "Crafting Stations"
		),
		FavoriteChip(
			option = FavoriteCategory.TOOL,
			icon = Lucide.Gavel,
			label = "Tools"
		),
		FavoriteChip(
			option = FavoriteCategory.MEAD,
			icon = Lucide.FlaskRound,
			label = "Meads"
		),
		FavoriteChip(
			option = FavoriteCategory.POINTOFINTEREST,
			icon = Lucide.MapPinned,
			label = "Points Of Interest"
		),
		FavoriteChip(
			option = FavoriteCategory.TREE,
			icon = Lucide.Trees,
			label = "Trees"
		),
		FavoriteChip(
			option = FavoriteCategory.OREDEPOSITE,
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
			uiState = UiState.Loading()
		)
	}
}

