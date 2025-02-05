package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BiomeScreen(
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Text("BiomeScreen")

}

@Preview(
    name = "BiomeDetail",
    showBackground = true
    )
@Composable
private fun PreviewBiomeDetail() {
    BiomeScreen()
}