package com.rabbitv.valheimviki.presentation.biome

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
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import com.rabbitv.valheimviki.presentation.base.UiState
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatureScreen(
    viewModel: CreaturesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                when (uiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is UiState.Success -> {
                        val creatures = (uiState as UiState.Success<List<CreatureDtoX>>).data
                        CreatureList(creatures = creatures)
                    }

                    is UiState.Error -> {
                        val errorMessage = (uiState as UiState.Error).message
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
    }
}

@Composable
fun CreatureList(
    creatures: List<CreatureDtoX>,
    modifier: Modifier = Modifier
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
        items(creatures) { creature ->
            CreatureItem(creature = creature)
            HorizontalDivider()
        }
    }
}

@Composable
fun CreatureItem(
    creature: CreatureDtoX,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = creature.name.toString(), style = MaterialTheme.typography.bodyMedium)
        creature.summoningItems?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = creature.note.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(text = creature.biomeId, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewCreatureScreen() {
    val sampleCreatures = emptyList<CreatureDtoX>()
    val uiState = UiState.Success(sampleCreatures)

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