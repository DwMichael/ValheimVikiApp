package com.rabbitv.valheimviki.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "SplashScreen")
    }
}

@Preview(name = "SplashScreen")
@Composable
private fun PreviewSplashScreen() {
    SplashScreen()
}