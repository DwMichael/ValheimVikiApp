package com.rabbitv.valheimviki.presentation.creatures.bosses


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
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.domain.model.creature.Creature

import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.common.EmptyScreen
import com.rabbitv.valheimviki.presentation.common.GridContent
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.NORMAL_SIZE_GRID
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BossScreen(
    paddingValues: PaddingValues,
    viewModel: BossesViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val bossUIState: BossUIState by viewModel.bossUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()


    if (bossUIState.isLoading) {
        LoadingIndicator(paddingValues = paddingValues)
    } else {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .testTag("BossSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (bossUIState.bosses.isEmpty()) {
                false -> {
                    Box(
                        modifier = Modifier.testTag("BossGrid"),
                    ) {
                        GridContent(
                            modifier = Modifier,
                            items = bossUIState.bosses,
                            clickToNavigate = { item ->
                                navController.navigate(Screen.CreatureDetail.passCreatureId(mainBossId = item.id))
                                navController.navigate(Screen.CreatureDetail.passCreatureId(mainBossId = item.id))
                            },
                            state = refreshState,
                            onRefresh = {
                                viewModel.refetchBosses()
                                scope.launch {
                                    refreshState.animateToHidden()
                                }
                            },
                            isRefreshing = refreshing,
                            numbersOfColumns = NORMAL_SIZE_GRID,
                            height = ITEM_HEIGHT_TWO_COLUMNS
                        )
                    }
                }

                true -> {
                    Box(
                        modifier = Modifier.testTag("EmptyScreenBoss"),
                    ) {
                        EmptyScreen(
                            modifier = Modifier,
                            state = refreshState,
                            isRefreshing = refreshing,
                            onRefresh = {
                                viewModel.refetchBosses()
                                scope.launch {
                                    refreshState.animateToHidden()
                                }
                            },
                            errorMessage = bossUIState.error.toString()
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