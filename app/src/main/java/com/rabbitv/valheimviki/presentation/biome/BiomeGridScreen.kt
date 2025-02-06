package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.components.GridContent
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeGridScreen(
    paddingValues: PaddingValues,
    viewModel: BiomeListScreenViewModel = hiltViewModel(),
    navController: NavHostController,

    ) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val biomeUIState: BiomesUIState by viewModel.biomeUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (biomeUIState.isLoading) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GridContent(
                items = biomeUIState.biomes,
                modifier = Modifier,
                clickToNavigate = { item ->
                    navController.navigate(Screen.Biome.passBiomeId(biomeId = item.id))
                },
                state = refreshState,
                onRefresh = {
                    viewModel.load()
                    scope.launch {
                        refreshState.animateToHidden()
                    }
                },
                isRefreshing = refreshing,
                numbersOfColumns = BIOME_GRID_COLUMNS,
                height = ITEM_HEIGHT_TWO_COLUMNS
            )

        }
    }

}


@Composable
private fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

