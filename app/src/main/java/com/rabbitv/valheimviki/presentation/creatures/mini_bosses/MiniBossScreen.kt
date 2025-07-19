package com.rabbitv.valheimviki.presentation.creatures.mini_bosses


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.viewmodel.MiniBossesViewModel
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun MiniBossScreen(
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: MiniBossesViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val uiState by viewModel.miniBossUiListState.collectAsStateWithLifecycle()
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
                .testTag("MiniBossSurface")
                .fillMaxSize()
                .padding(paddingValues)
		) {
			when (val state = uiState) {
				is UIState.Loading -> ShimmerGridEffect()
				is UIState.Error -> EmptyScreen(errorMessage = state.message)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewMiniBossListScreen() {
	emptyList<Creature>()

	Scaffold(
		topBar = {
			TopAppBar(title = { Text("Biomes") })
		},
		content = { padding ->
			Box(
				modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
			) {

			}
		}
	)
}