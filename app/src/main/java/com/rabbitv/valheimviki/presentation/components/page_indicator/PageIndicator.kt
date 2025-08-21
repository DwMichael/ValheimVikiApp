package com.rabbitv.valheimviki.presentation.components.page_indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
	pagerState: PagerState,
) {
	Row(
		Modifier
			.wrapContentHeight()
			.fillMaxWidth()
			.padding(8.dp),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.Bottom
	) {
		repeat(pagerState.pageCount) { iteration ->
			val color =
				if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
			Box(
				modifier = Modifier
					.padding(2.dp)
					.clip(CircleShape)
					.background(color)
					.size(8.dp)
			)
		}
	}
}