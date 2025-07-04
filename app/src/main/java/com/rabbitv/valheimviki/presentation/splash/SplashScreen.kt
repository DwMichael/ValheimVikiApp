package com.rabbitv.valheimviki.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage


@Composable
fun SplashScreen(
	navController: NavHostController,
	splashViewModel: SplashViewModel = hiltViewModel()
) {
	val degrees = remember { Animatable(0f) }
	val hasOnboarded by splashViewModel.onBoardingCompleted.collectAsState(initial = false)
	LaunchedEffect(hasOnboarded) {
		degrees.animateTo(
			targetValue = 10f,
			animationSpec = tween(
				delayMillis = 1
			)
		)
		val destination =
			if (hasOnboarded) Screen.BiomeList else Screen.Welcome
		navController.navigate(destination) {
			popUpTo(0) { inclusive = true }
		}
	}
	BgImage()
}

@Preview(name = "SplashScreen")
@Composable
private fun PreviewSettingsScreen() {
	SplashScreen(navController = NavHostController(LocalContext.current))
}