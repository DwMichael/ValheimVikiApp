package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BiomeScreen(
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    Text("BiomeScreen")

}

@Preview(
    name = "BiomeDetail",
    showBackground = true
)
@Composable
private fun PreviewBiomeDetail() {
    BiomeScreen(paddingValues = PaddingValues(0.dp))
}