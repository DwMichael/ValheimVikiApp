package com.rabbitv.valheimviki.presentation.material

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Amphora
import com.composables.icons.lucide.BadgeCent
import com.composables.icons.lucide.BadgeDollarSign
import com.composables.icons.lucide.BadgeIndianRupee
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mountain
import com.composables.icons.lucide.Package
import com.composables.icons.lucide.Sprout
import com.composables.icons.lucide.TreeDeciduous
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import kotlinx.coroutines.launch

class MaterialChip(
	override val option: MaterialSubType,
	override val icon: ImageVector,
	override val label: String
) : ChipData<MaterialSubType>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialListScreen(
	onBackClick: () -> Unit,
	onItemClick: (String, MaterialSubCategory) -> Unit,
	viewModel: MaterialListViewModel,
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val lazyListState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val title = uiState.selectedCategory?.let { viewModel.getLabelFor(it) }
	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}

	BackHandler(onBack = {
		viewModel.onCategorySelected(null)
		viewModel.onTypeSelected(null)
		onBackClick()
	})
	Scaffold(
		topBar = {
			SimpleTopBar(
				modifier = Modifier,
				title = title,
				onClick = {
					viewModel.onCategorySelected(null)
					viewModel.onTypeSelected(null)
					onBackClick()
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
				}
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		content = { innerScaffoldPadding ->
			when (val currentState = uiState) {
				is UiCategoryChipState.Loading -> {
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

				is UiCategoryChipState.Success -> {
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
								if (currentState.selectedCategory != null) {
									SearchFilterBar(
										chips = getChipsForCategory(currentState.selectedCategory),
										selectedOption = currentState.selectedChip,
										onSelectedChange = { _, subCategoryType ->
											if (currentState.selectedChip == subCategoryType) {
												viewModel.onTypeSelected(null)
											} else {
												viewModel.onTypeSelected(subCategoryType)
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
								if (currentState.selectedCategory != null) {
									ListContent(
										items = currentState.list,
										clickToNavigate = onItemClick,
										lazyListState = lazyListState,
										subCategoryNumber = currentState.selectedCategory,
										imageScale = ContentScale.Fit,
										horizontalPadding = 0.dp
									)
								}
							}
						}
					}
				}

				is UiCategoryChipState.Error -> {
					EmptyScreen(
						errorMessage = currentState.message
					)
				}
			}
		}
	)
}


@Composable
private fun getChipsForCategory(category: MaterialSubCategory?): List<MaterialChip> { // Changed to nullable
	return when (category) { // category can now be null
		MaterialSubCategory.BOSS_DROP -> listOf(
			MaterialChip(
				MaterialSubType.ITEM,
				Lucide.Amphora,
				"Item"
			),
			MaterialChip(
				MaterialSubType.TROPHY,
				Lucide.Trophy,
				"Trophy"
			)
		)

		MaterialSubCategory.MINI_BOSS_DROP -> emptyList()
		MaterialSubCategory.CREATURE_DROP -> listOf(
			MaterialChip(
				MaterialSubType.TROPHY,
				Lucide.Trophy,
				"Trophy"
			),
			MaterialChip(
				MaterialSubType.LOOT,
				Lucide.Package,
				"Loot"
			),
		)

		MaterialSubCategory.FORSAKEN_ALTAR_OFFERING -> emptyList()
		MaterialSubCategory.CRAFTED -> emptyList()
		MaterialSubCategory.MISCELLANEOUS -> emptyList()
		MaterialSubCategory.GEMSTONE -> emptyList()
		MaterialSubCategory.SEED -> listOf(
			MaterialChip(
				MaterialSubType.CROP,
				Lucide.Sprout,
				"Crop Seed"
			),
			MaterialChip(
				MaterialSubType.TREE,
				Lucide.TreeDeciduous,
				"Tree Seed"
			),
		)

		MaterialSubCategory.METAL -> listOf(
			MaterialChip(
				MaterialSubType.INGOTS,
				Lucide.Cuboid,
				"Ingots"
			),
			MaterialChip(MaterialSubType.ORES, Lucide.Mountain, "Ores")
		)

		MaterialSubCategory.SHOP -> listOf(
			MaterialChip(
				MaterialSubType.HALDOR,
				Lucide.BadgeCent,
				"Haldor Items"
			),
			MaterialChip(
				MaterialSubType.HILDIR,
				Lucide.BadgeDollarSign,
				"Hildir Items"
			),
			MaterialChip(
				MaterialSubType.BOG_WITCH,
				Lucide.BadgeIndianRupee,
				"Bog Witch Items"
			),
		)

		MaterialSubCategory.VALUABLE -> emptyList()
		null -> emptyList() // Handle the null case explicitly
	}
}
