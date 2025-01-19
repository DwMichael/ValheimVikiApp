package com.rabbitv.valheimviki.presentation.biome

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BiomeScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "BiomesPage")
    }
}

@Preview(name = "BiomesPage")
@Composable
private fun PreviewBiomePage() {
    BiomeScreen()
}