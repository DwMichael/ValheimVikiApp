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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.ui_state.UiState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.viewmodel.MiniBossesViewModel
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.NORMAL_SIZE_GRID
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun MiniBossScreen(
    modifier: Modifier,
    onItemClick: (String, String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: MiniBossesViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope

) {
    val miniBossUiState: UiState<MiniBoss> by viewModel.miniBossUiState.collectAsStateWithLifecycle()
    val scrollPosition = rememberSaveable { mutableIntStateOf(0) }
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
                .testTag("MiniBossSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = miniBossUiState) {
                is UiState.Loading -> ShimmerGridEffect()
                is UiState.Error -> EmptyScreen(errorMessage = state.message.toString())
                is UiState.Success -> DefaultGrid(
                    modifier = Modifier,
                    items = state.list,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewMiniBossListScreen() {
    val sampleCreatures = emptyList<Creature>()

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