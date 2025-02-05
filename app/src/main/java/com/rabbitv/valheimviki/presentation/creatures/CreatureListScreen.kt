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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.creatures.CreaturesUIState
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatureListScreen(
    viewModel: CreaturesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val creatureUIState: CreaturesUIState by viewModel.creatureUIState.collectAsStateWithLifecycle()
    val refreshing: Boolean by viewModel.isRefreshing.collectAsStateWithLifecycle()


    if (creatureUIState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Surface {
            CreatureList(
                creatures = creatureUIState.creatures,
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
fun CreatureList(
    creatures: List<CreatureDtoX>,
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
            if (creatures.isEmpty()) {
                items(creatures) { creature ->
                    CreatureItem(creature = creature, navController = navController)
                    HorizontalDivider()
                }
            } else {
                items(creatures) { creature ->
                    CreatureItem(creature = creature, navController = navController)
                    HorizontalDivider()
                }

            }
        }
    }
}

@Composable
fun CreatureItem(
    creature: CreatureDtoX,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(Screen.Creature.passCreatureId(creatureId = creature.creatureId))
            },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = creature.name.toString(), style = MaterialTheme.typography.bodyMedium)
            creature.summoningItems?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = creature.note.toString(), style = MaterialTheme.typography.bodyMedium)
            Text(text = creature.biomeId, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewCreatureScreen() {
    val sampleCreatures = emptyList<CreatureDtoX>()


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