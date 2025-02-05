package com.rabbitv.valheimviki.presentation.biome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.navigation.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeListScreen(
    viewModel: BiomeListScreenViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val biomeUIState: BiomesUIState by viewModel.biomeUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()


    if (biomeUIState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Surface {
            BiomeList(
                biomes = biomeUIState.biomes,
                modifier = Modifier,
                state = refreshState,
                onRefresh = {
                    viewModel.load()
                    scope.launch {
                        refreshState.animateToHidden()
                    }
                },
                isRefreshing = refreshing,
                navController = navController
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeList(
    biomes: List<BiomeDtoX>,
    modifier: Modifier = Modifier,
    state: PullToRefreshState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    navController: NavHostController
) {
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,

        ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .absolutePadding(
                    left = 16.dp,
                    top = 0.dp,
                    right = 16.dp,
                    bottom = 16.dp
                )
        ) {
            if (biomes.isEmpty()) {
                items(biomes) {
                    Text(
                        text = "Sprawdź połączenie z internetem",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                items(biomes) { biome ->
                    BiomeItem(biome = biome, navController = navController)
                    HorizontalDivider()
                }
            }

        }
    }
}

@Composable
fun BiomeItem(
    biome: BiomeDtoX,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(Screen.Biome.passBiomeId(biomeId = biome.biomeId))
            },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = biome.nameContent, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = biome.descriptionContent, style = MaterialTheme.typography.bodyMedium)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewBiomeScreen() {
    val navController = NavHostController(LocalContext.current)
    val sampleBiomes = listOf(
        BiomeDtoX(
            biomeId = "123123",
            nameContent = "Forest", descriptionContent = "A dense and lush forest.",

            stage = Stage.MID.toString(),
            imageUrl = "",
            order = 1
        ),
        BiomeDtoX(
            biomeId = "123123",
            nameContent = "Desert", descriptionContent = "A vast and arid desert.",

            stage = Stage.EARLY.toString(),
            imageUrl = "",
            order = 2
        ),

        )


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp),
                title = { Text("Biomes") })
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

//                BiomeList(biomes = sampleBiomes, navController = navController)
            }
        }
    )
}