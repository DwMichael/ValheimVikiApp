package com.rabbitv.valheimviki.presentation.tool

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Diamond
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.TestTubeDiagonal
import com.composables.icons.lucide.Wheat
import com.composables.icons.lucide.Wrench
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.tool.model.ToolChip
import com.rabbitv.valheimviki.presentation.tool.viewmodel.ToolListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.utils.toAppCategory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolListScreen(
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: ToolListViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val lazyListState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}


	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("FoodListSurface")
			.fillMaxSize()
			.padding(paddingValues)
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Column(
				modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				SearchFilterBar(
					chips = getChipsForCategory(),
					selectedOption = uiState.selectedCategory,
					onSelectedChange = { _, subCategory ->
						if (uiState.selectedCategory == subCategory) {
							viewModel.onChipSelected(null)
						} else {
							viewModel.onChipSelected(subCategory)
						}
					},
					modifier = Modifier,
				)
				Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))

				Box(modifier = Modifier.fillMaxSize()) {
					when (val state = uiState) {
						is UiCategoryState.Error -> EmptyScreen(errorMessage = state.message.toString())
						is UiCategoryState.Loading -> {
							Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
							ShimmerListEffect()
						}

						is UiCategoryState.Success -> ListContent(
							items = state.list,
							clickToNavigate = { itemData ->
								val destination = NavigationHelper.routeToDetailScreen(
									itemData,
									itemData.category.toAppCategory()
								)
								onItemClick(destination)
							},
							lazyListState = lazyListState,
							imageScale = ContentScale.Fit,
							horizontalPadding = 0.dp
						)
					}
				}
			}

			CustomFloatingActionButton(
				showBackButton = backButtonVisibleState,
				onClick = {
					scope.launch {
						lazyListState.animateScrollToItem(0)
					}
				},
				modifier = Modifier
					.align(Alignment.BottomEnd)
					.padding(BODY_CONTENT_PADDING.dp)
			)
		}
	}
}


@Composable
private fun getChipsForCategory(): List<ToolChip> {
	return listOf(
		ToolChip(
			ToolSubCategory.MEAD_CONSUMPTION,
			Lucide.TestTubeDiagonal,
			"Mead consumption"
		),
		ToolChip(
			ToolSubCategory.BUILDING,
			Lucide.Wrench,
			"Building"
		),
		ToolChip(
			ToolSubCategory.PICKAXES,
			Lucide.Pickaxe,
			"Pickaxe"
		),
		ToolChip(
			ToolSubCategory.TRAVERSAL,
			Lucide.Diamond,
			"Accessories"
		),
		ToolChip(
			ToolSubCategory.FARMING,
			Lucide.Wheat,
			"Farming"
		),
		ToolChip(
			ToolSubCategory.FISHING,
			ImageVector.vectorResource(id = R.drawable.fishing_rod),
			"Fishing"
		),
	)
}
