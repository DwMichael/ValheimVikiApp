package com.rabbitv.valheimviki.presentation.creatures.bosses


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
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.NORMAL_SIZE_GRID
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BossScreen(
    modifier: Modifier,
    onItemClick: (String, String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: BossesViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val uiState: BossUiState by viewModel.bossUIState.collectAsStateWithLifecycle()
    val isConnection: Boolean by viewModel.isConnection.collectAsStateWithLifecycle()

    val initialScrollPosition by viewModel.scrollPosition.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialScrollPosition
    )

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .distinctUntilChanged()
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
                .testTag("BossSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading || (uiState.bosses.isEmpty() && isConnection) -> {
                    ShimmerGridEffect()
                }

                uiState.bosses.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.testTag("BossGrid"),
                    ) {
                        DefaultGrid(
                            modifier = Modifier,
                            items = uiState.bosses,
                            onItemClick = onItemClick,
                            numbersOfColumns = NORMAL_SIZE_GRID,
                            height = ITEM_HEIGHT_TWO_COLUMNS,
                            animatedVisibilityScope = animatedVisibilityScope,
                            lazyGridState = lazyGridState
                        )
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.testTag("EmptyScreenBoss"),
                    ) {
                        EmptyScreen(
                            errorMessage = uiState.error.toString()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewBossListScreen() {
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