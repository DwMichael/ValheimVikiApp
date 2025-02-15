package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun BiomeDetailScreen(
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Text("BiomeScreen")
    }

}

@Preview(
    name = "BiomeDetail",
    showBackground = true
)
@Composable
private fun PreviewBiomeDetail() {
    BiomeDetailScreen(paddingValues = PaddingValues(0.dp))
}