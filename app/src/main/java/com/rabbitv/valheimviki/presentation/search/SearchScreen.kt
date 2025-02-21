package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "SearchScreen")
    }
}

@Preview(name = "SearchScreen")
@Composable
private fun PreviewSearchScreen() {
    SearchScreen()
}