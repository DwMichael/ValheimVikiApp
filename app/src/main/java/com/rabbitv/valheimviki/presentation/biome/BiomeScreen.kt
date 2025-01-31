package com.rabbitv.valheimviki.presentation.biome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.presentation.base.UiState



@Composable
fun BiomeScreen(
    viewModel: BiomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Surface{
        Box {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    val biomes = (uiState as UiState.Success<List<BiomeDtoX>>).data
                    BiomeList(biomes = biomes)
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
fun BiomeList(
    biomes: List<BiomeDtoX>,
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
        items(biomes) { biome ->
            BiomeItem(biome = biome)
            HorizontalDivider()
        }
    }
}

@Composable
fun BiomeItem(
    biome: BiomeDtoX,
    modifier: Modifier = Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewBiomeScreen() {
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
            nameContent = "Desert", descriptionContent =  "A vast and arid desert.",

            stage = Stage.EARLY.toString(),
            imageUrl = "",
            order = 2
        ),

    )
    val uiState = UiState.Success(sampleBiomes)

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
                BiomeList(biomes = sampleBiomes)
            }
        }
    )
}