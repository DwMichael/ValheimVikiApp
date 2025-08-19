package com.rabbitv.valheimviki.presentation.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ForestGreen30Dark

@Composable
fun Modifier.lazyVerticalScrollbar(
	lazyListState: LazyListState,
	width: Dp = 4.dp,
	showScrollBarTrack: Boolean = true,
	scrollBarTrackColor: Color = ForestGreen30Dark,
	scrollBarColor: Color = ForestGreen10Dark,
	scrollBarCornerRadius: Float = 4f,
	endPadding: Float = 8f,
	fadeOutDelay: Long = 1000L
): Modifier {
	val isScrollInProgress by remember {
		derivedStateOf { lazyListState.isScrollInProgress }
	}

	val alpha by animateFloatAsState(
		targetValue = if (isScrollInProgress) 1f else 0f,
		animationSpec = tween(durationMillis = 300),
		label = "scrollbar_alpha"
	)

	return drawWithContent {
		drawContent()

		val layoutInfo = lazyListState.layoutInfo
		val viewportHeight = layoutInfo.viewportSize.height.toFloat()
		val totalItemsCount = layoutInfo.totalItemsCount
		val visibleItemsCount = layoutInfo.visibleItemsInfo.size

		if (totalItemsCount <= visibleItemsCount || alpha == 0f) return@drawWithContent

		val firstVisibleItemIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
		layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

		val visibleItemsRatio = visibleItemsCount.toFloat() / totalItemsCount.toFloat()
		val scrollBarHeight =
			(visibleItemsRatio * viewportHeight).coerceAtLeast(30f)

		val scrollProgress =
			firstVisibleItemIndex.toFloat() / (totalItemsCount - visibleItemsCount).toFloat()
		val scrollBarStartOffset = scrollProgress * (viewportHeight - scrollBarHeight)

		if (showScrollBarTrack) {
			drawRoundRect(
				cornerRadius = CornerRadius(scrollBarCornerRadius),
				color = scrollBarTrackColor.copy(alpha = scrollBarTrackColor.alpha * alpha),
				topLeft = Offset(this.size.width - endPadding - width.toPx(), 0f),
				size = Size(width.toPx(), viewportHeight),
			)
		}

		drawRoundRect(
			cornerRadius = CornerRadius(scrollBarCornerRadius),
			color = scrollBarColor.copy(alpha = scrollBarColor.alpha * alpha),
			topLeft = Offset(this.size.width - endPadding - width.toPx(), scrollBarStartOffset),
			size = Size(width.toPx(), scrollBarHeight)
		)
	}
}
