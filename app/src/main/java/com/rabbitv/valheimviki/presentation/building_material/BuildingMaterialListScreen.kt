package com.rabbitv.valheimviki.presentation.building_material

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Archive
import com.composables.icons.lucide.Armchair
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Box
import com.composables.icons.lucide.Cog
import com.composables.icons.lucide.Flag
import com.composables.icons.lucide.Grid2x2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sailboat
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Table
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialUiEvent
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.launch

@Stable
class BuildingMaterialChip(
	override val option: BuildingMaterialSubType,
	override val icon: ImageVector,
	override val label: String
) : ChipData<BuildingMaterialSubType>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingMaterialListScreen(
	onBackClick: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: BuildingMaterialListViewModel,
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val lazyListState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val title = uiState.selectedCategory?.let { viewModel.getLabelFor(it) }

	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}
	val handleFavoriteItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	BackHandler(onBack = {
		viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(null))
		viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(null))
		onBackClick()
	})

	Scaffold(
		topBar = {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 40.dp)
			) {
				IconButton(
					onClick = {
						viewModel.onEvent(BuildingMaterialUiEvent.CategorySelected(null))
						viewModel.onEvent(BuildingMaterialUiEvent.ChipSelected(null))
						onBackClick()
					},
					modifier = Modifier
						.align(Alignment.BottomStart)
						.padding(
							start = BODY_CONTENT_PADDING.dp,
							end = BODY_CONTENT_PADDING.dp
						),
					colors = IconButtonColors(
						containerColor = ForestGreen10Dark,
						contentColor = PrimaryWhite,
						disabledContainerColor = Color.Black,
						disabledContentColor = Color.Black,
					),
				) {
					Icon(
						Lucide.ArrowLeft,
						contentDescription = "Back"
					)
				}

				if (title != null) {
					Text(
						text = title,
						color = MaterialTheme.colorScheme.onPrimaryContainer,
						style = MaterialTheme.typography.headlineSmall,
						modifier = Modifier
							.align(Alignment.BottomCenter)
					)
				}
			}
		},
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
		content = { innerScaffoldPadding ->
			when (val currentState = uiState.materialsUiState) {
				is UIState.Loading -> {
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(innerScaffoldPadding),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Top
					) {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.padding(BODY_CONTENT_PADDING.dp)
						) {

							Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
							ShimmerListEffect()
						}
					}
				}

				is UIState.Success -> {
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(innerScaffoldPadding),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Top
					) {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.padding(BODY_CONTENT_PADDING.dp)
						) {
							Column(
								horizontalAlignment = Alignment.CenterHorizontally
							) {
								if (uiState.selectedCategory != null) {
									SearchFilterBar(
										chips = getChipsForCategory(uiState.selectedCategory),
										selectedOption = uiState.selectedChip,
										onSelectedChange = { _, subCategoryType ->
											viewModel.onEvent(
												BuildingMaterialUiEvent.ChipSelected(
													subCategoryType
												)
											)
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
								ListContent(
									items = currentState.data,
									clickToNavigate = handleFavoriteItemClick,
									lazyListState = lazyListState,
									imageScale = ContentScale.Fit,
									horizontalPadding = 0.dp
								)
							}
						}
					}
				}

				is UIState.Error -> {
					EmptyScreen(
						errorMessage = currentState.message
					)
				}
			}
		}
	)
}


@Composable
private fun getChipsForCategory(category: BuildingMaterialSubCategory?): List<BuildingMaterialChip> {
	return when (category) {
		BuildingMaterialSubCategory.STONE_AND_METAL -> emptyList()
		BuildingMaterialSubCategory.LIGHT_SOURCE -> emptyList()
		BuildingMaterialSubCategory.FURNITURE -> listOf(
			BuildingMaterialChip(
				BuildingMaterialSubType.STORAGE,
				Lucide.Archive,
				"Storage"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.FUNCTIONAL,
				Lucide.Cog,
				"Functional"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.TABLE,
				Lucide.Table,
				"Table"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.CHAIR,
				Lucide.Armchair,
				"Chair"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.RUG,
				Lucide.Grid2x2,
				"Rug"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.BANNER,
				Lucide.Flag,
				"Banner"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.DECORATIVE,
				Lucide.Star,
				"Decorative"
			)
		)


		null -> emptyList() // Handle the null case explicitly
		BuildingMaterialSubCategory.WOOD -> emptyList()
		BuildingMaterialSubCategory.CORE_WOOD -> emptyList()
		BuildingMaterialSubCategory.RESOURCE -> emptyList()
		BuildingMaterialSubCategory.TRANSPORT -> listOf(
			BuildingMaterialChip(
				BuildingMaterialSubType.MISC,
				Lucide.Box,
				"Misc"
			),
			BuildingMaterialChip(
				BuildingMaterialSubType.BOAT,
				Lucide.Sailboat,
				"Boat"
			)
		)

		BuildingMaterialSubCategory.DEFENSE -> emptyList()
		BuildingMaterialSubCategory.SIEGE -> emptyList()
		BuildingMaterialSubCategory.DECORATIVE -> emptyList()
		BuildingMaterialSubCategory.ROOF -> emptyList()
		BuildingMaterialSubCategory.MISC -> emptyList()
	}
}