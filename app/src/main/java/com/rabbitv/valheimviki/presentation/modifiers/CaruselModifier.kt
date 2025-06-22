package com.rabbitv.valheimviki.presentation.modifiers

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

fun Modifier.carouselEffect(
	pagerState: PagerState,
	pageIndex: Int
): Modifier = this.then(
	Modifier.graphicsLayer {
		val pageOffset =
			((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue
		val scale = lerp(start = 0.8f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
		scaleX = scale
		scaleY = scale
		alpha = lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
		cameraDistance = 8f * density
	}
)