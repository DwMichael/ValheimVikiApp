package com.rabbitv.valheimviki.presentation.trinkets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.trinkets.model.TrinketUiState
import com.rabbitv.valheimviki.presentation.trinkets.viewmodel.TrinketListViewModel

@Composable
fun TrinketListScreen(
	modifier: Modifier = Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: TrinketListViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()


	TrinketListStateRenderer(
		uiState = uiState,
		paddingValues = paddingValues,
		modifier = modifier,
		onItemClick = onItemClick
	)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrinketListStateRenderer(
	uiState: TrinketUiState,
	paddingValues: PaddingValues,
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
) {
	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("TrinketListSurface")
			.fillMaxSize()
			.padding(paddingValues)
			.then(modifier)
	) {
		TrinketListDisplay(
			uiState = uiState,
			onItemClick = onItemClick
		)
	}
}


@Composable
fun TrinketListDisplay(
	uiState: TrinketUiState,
	onItemClick: (destination: DetailDestination) -> Unit,
) {
	val lazyListState = rememberLazyListState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	Column(
		horizontalAlignment = Alignment.Start
	) {
		when (uiState.trinketsUiState) {
			is UIState.Error -> EmptyScreen(errorMessage = stringResource(id = uiState.trinketsUiState.message.toInt()))
			is UIState.Loading -> ShimmerListEffect()
			is UIState.Success -> {
				ListContent(
					items = uiState.trinketsUiState.data,
					clickToNavigate = handleItemClick,
					lazyListState = lazyListState,
					imageScale = ContentScale.Fit,
					horizontalPadding = 0.dp
				)
			}
		}
	}
}


