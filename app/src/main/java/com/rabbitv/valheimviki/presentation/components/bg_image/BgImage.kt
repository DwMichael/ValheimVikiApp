package com.rabbitv.valheimviki.presentation.components.bg_image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.rabbitv.valheimviki.R

@Composable
fun BgImage() {
	val img = ImageBitmap.imageResource(R.drawable.main_background)
//	val painter = painterResource(R.drawable.main_background)
	val painter = remember(img) {
		BitmapPainter(img, filterQuality = FilterQuality.Low)
	}

	Box(
		Modifier
			.fillMaxSize()
			.paint(
				painter = painter,
				contentScale = ContentScale.Crop
			)
	)
}