package com.rabbitv.valheimviki.presentation.core

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SecondScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "SecondPage")
    }
}

@Preview(name = "SecondPage")
@Composable
private fun PreviewSecondPage() {
    SecondScreen()
}