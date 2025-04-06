package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.common.EmptyScreen
import com.rabbitv.valheimviki.presentation.common.GridContent
import com.rabbitv.valheimviki.presentation.components.ShimmerEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import com.rabbitv.valheimviki.utils.isNetworkAvailable
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeScreen(
    paddingValues: PaddingValues,
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    navController: NavHostController,

    ) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val biomeUIState: BiomesUIState by viewModel.biomeUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isConnection: Boolean = isNetworkAvailable(LocalContext.current)


    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("BiomeSurface")
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        when {
            biomeUIState.isLoading -> {
                ShimmerEffect()
            }

            biomeUIState.error != null -> {
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
                        errorMessage = "Can't fetch data from server try later"
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
                        clickToNavigate = { item ->
                            navController.navigate(Screen.BiomeDetail.passBiomeId(biomeId = item.id))
                        },
                        numbersOfColumns = BIOME_GRID_COLUMNS,
                        height = ITEM_HEIGHT_TWO_COLUMNS
                    )
                }
            }

            else -> {
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
                        errorMessage = if (!isConnection) "No internet connection" else "Can't fetch data from server try later"
                    )
                }
            }
        }
    }
}




