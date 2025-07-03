package com.rabbitv.valheimviki.presentation.components.bg_image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.rabbitv.valheimviki.R

@Composable
fun BgImage() {
	val painterBackgroundImage = painterResource(R.drawable.main_background)
	Image(
		painter = painterBackgroundImage,
		contentDescription = "BackgroundImage",
		contentScale = ContentScale.Crop,
		modifier = Modifier.fillMaxSize(),
	)
}