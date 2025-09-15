package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.biome.viewmodel.BiomeGridViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(FlowPreview::class)
@Composable
fun BiomeGridScreen(
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: BiomeGridViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val uiState: UIState<List<Biome>> by viewModel.uiState.collectAsStateWithLifecycle()
	val lazyGridState = rememberLazyGridState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	Box(
		modifier = modifier
	) {
		Surface(
			color = Color.Transparent,
			modifier = Modifier
				.testTag("BiomeSurface")
				.fillMaxSize()

				.padding(paddingValues)
		) {
			when (val state = uiState) {
				is UIState.Loading -> ShimmerGridEffect()
				is UIState.Error -> EmptyScreen(
					errorMessage = stringResource(id = state.message.toInt()),
				)

				is UIState.Success -> DefaultGrid(
					modifier = Modifier,
					items = state.data,
					onItemClick = handleItemClick,
					numbersOfColumns = BIOME_GRID_COLUMNS,
					height = ITEM_HEIGHT_TWO_COLUMNS,
					animatedVisibilityScope = animatedVisibilityScope,
					lazyGridState = lazyGridState,
				)
			}
		}
	}
}




