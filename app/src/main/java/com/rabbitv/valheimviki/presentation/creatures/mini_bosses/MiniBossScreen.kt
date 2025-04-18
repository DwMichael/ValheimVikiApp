package com.rabbitv.valheimviki.presentation.creatures.mini_bosses


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.GridContent
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.NORMAL_SIZE_GRID
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossScreen(
    modifier : Modifier,
    onItemClick :(String,String)-> Unit,
    paddingValues: PaddingValues,
    viewModel: MiniBossesViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope

) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val miniBossesUIState: MiniBossesUIState by viewModel.miniBossesUIState
        .collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()


    if (miniBossesUIState.isLoading) {
        LoadingIndicator(paddingValues = paddingValues)
    } else {
        Box(
            modifier= modifier
        ) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .testTag("MiniBossSurface")
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (miniBossesUIState.miniBosses.isEmpty()) {
                    false -> {
                        Box(
                            modifier = Modifier.testTag("MiniBossGird"),
                        ) {
                            GridContent(
                                modifier = Modifier,
                                items = miniBossesUIState.miniBosses,
                                onItemClick = onItemClick ,
                                numbersOfColumns = NORMAL_SIZE_GRID,
                                height = ITEM_HEIGHT_TWO_COLUMNS,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }

                    true -> {
                        Box(
                            modifier = Modifier.testTag("EmptyScreenMiniBoss"),
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
                                errorMessage = miniBossesUIState.error.toString()
                            )
                        }
                    }
                }


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