package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.GridContent
import com.rabbitv.valheimviki.presentation.components.ShimmerEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun BiomeScreen(
    modifier: Modifier,
    onItemClick : (String, String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
    ) {

    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val biomeUIState: BiomesUIState by viewModel.biomeUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isConnection: Boolean = viewModel.isConnection

    val initialScrollPosition by viewModel.scrollPosition.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialScrollPosition
    )

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .debounce(500L)
            .collectLatest { index ->
                if (index >= 0) {
                    viewModel.saveScrollPosition(index)
                }
            }
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
            when {
                biomeUIState.isLoading || (biomeUIState.biomes.isEmpty() && isConnection)  -> {
                    ShimmerEffect()
                }

                biomeUIState.error != null && biomeUIState.biomes.isEmpty() -> {
                    Box(
                        modifier = Modifier.testTag("EmptyScreenBiome"),
                    ) {
                        EmptyScreen(
                            modifier = Modifier,
                            state = refreshState,
                            isRefreshing = refreshing,
                            onRefresh = {
                                viewModel.refetchBiomes()
                                scope.launch {
                                    refreshState.animateToHidden()
                                }
                            },
                            errorMessage = "Unexpected error occurred, pull down to refresh"
                        )
                    }
                }

                biomeUIState.biomes.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.testTag("BiomeGrid"),
                    ) {
                        GridContent(
                            modifier = Modifier,
                            items = biomeUIState.biomes,
                            onItemClick = onItemClick,
                            numbersOfColumns = BIOME_GRID_COLUMNS,
                            height = ITEM_HEIGHT_TWO_COLUMNS,
                            animatedVisibilityScope = animatedVisibilityScope,
                            lazyGridState = lazyGridState,
                        )
                    }
                }
            }
        }
    }
}




