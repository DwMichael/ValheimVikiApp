package com.rabbitv.valheimviki.presentation.points_of_interest

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.FlaskConical
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Soup
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiListUiEvent
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiSegmentOption
import com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel.PoiListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiListScreen(
	onItemClick: (destination: DetailDestination) -> Unit,
	modifier: Modifier, paddingValues: PaddingValues,
	viewModel: PoiListViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val scope = rememberCoroutineScope()
	val lazyListState = rememberLazyListState()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}

	val icons: List<ImageVector> = listOf(
		Lucide.Soup,
		Lucide.FlaskConical,
	)
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}


	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("MeadListSurface")
			.fillMaxSize()
			.padding(paddingValues)
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Column(
				modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				SegmentedButtonSingleSelect(
					options = PoiSegmentOption.entries,
					selectedOption = uiState.selectedSubCategory,
					onOptionSelected = {
						viewModel.onEvent(PoiListUiEvent.CategorySelected(it))
						scope.launch {
							lazyListState.animateScrollToItem(0)
						}
					},
					icons = icons
				)
				Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
				Box(modifier = Modifier.fillMaxSize()) {
					when (val state = uiState.poiListState) {
						is UIState.Loading -> {
							Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
							ShimmerListEffect()
						}

						is UIState.Error -> EmptyScreen(errorMessage = state.message)
						is UIState.Success -> ListContent(
							items = state.data,
							clickToNavigate = handleItemClick,
							lazyListState = lazyListState,
							horizontalPadding = 0.dp,
							imageScale = ContentScale.Crop,
							bottomBosPadding = 50.dp
						)
					}

					CustomFloatingActionButton(
						modifier = Modifier
							.align(Alignment.BottomEnd)
							.padding(BODY_CONTENT_PADDING.dp),
						showBackButton = backButtonVisibleState,
						onClick = {
							scope.launch {
								lazyListState.animateScrollToItem(0)
							}
						},
					)
				}
			}
		}
	}
}




