package com.rabbitv.valheimviki.presentation.food

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Rat
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.User
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.food.model.FoodSegmentOption
import com.rabbitv.valheimviki.presentation.food.viewmodel.FoodListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.utils.toAppCategory
import kotlinx.coroutines.launch


@Composable
fun FoodListScreen(
	modifier: Modifier, paddingValues: PaddingValues,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: FoodListViewModel = hiltViewModel()
) {
	val icons: List<ImageVector> = listOf(
		Lucide.Rat,
		Lucide.Skull,
		Lucide.User
	)
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
				SegmentedButtonSingleSelect(
					options = FoodSegmentOption.entries,
					selectedOption = uiState.selectedCategory,
					onOptionSelected = {
						viewModel.onCategorySelected(it)
						scope.launch {
							lazyListState.animateScrollToItem(0)
						}
					},
					icons = icons
				)
				Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
				Box(modifier = Modifier.fillMaxSize()) {
					when (val state = uiState) {
						is UiCategoryState.Loading -> {
							Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
							ShimmerListEffect()
						}

						is UiCategoryState.Error -> EmptyScreen(errorMessage = state.message.toString())
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
							horizontalPadding = 0.dp,
							imageScale = ContentScale.Fit
						)
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
	}
}




