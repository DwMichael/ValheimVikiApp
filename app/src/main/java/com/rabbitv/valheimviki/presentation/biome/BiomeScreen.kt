package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.UIState
import com.rabbitv.valheimviki.presentation.biome.viewmodel.BiomeScreenViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun BiomeScreen(
    modifier: Modifier,
    onItemClick: (String, String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {


    val biomeUIState: UIState<Biome> by viewModel.biomeUIState.collectAsStateWithLifecycle()
    val scrollPosition = remember { mutableIntStateOf(0) }


    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .debounce(500L)
            .collectLatest { index ->
                if (index >= 0) {
                    scrollPosition.intValue = index
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
                biomeUIState.isLoading || (biomeUIState.list.isEmpty() && biomeUIState.isConnection) -> {
                    ShimmerGridEffect()
                }

                (biomeUIState.error != null || !biomeUIState.isConnection) && biomeUIState.list.isEmpty() -> {
                    Box(
                        modifier = Modifier.testTag("EmptyScreenBiome"),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("EmptyScreenBiome")
                        ) {
                            EmptyScreen(
                                modifier = Modifier.fillMaxSize(),
                                errorMessage = biomeUIState.error
                                    ?: stringResource(R.string.no_internet_connection_ms)
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.testTag("BiomeGrid"),
                    ) {
                        DefaultGrid(
                            modifier = Modifier,
                            items = biomeUIState.list,
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




