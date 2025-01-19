package com.rabbitv.valheimviki.presentation.core

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "WelcomePage")
    }
}

@Preview(name = "WelcomePage")
@Composable
private fun PreviewWelcomePage() {
    WelcomeScreen()
}