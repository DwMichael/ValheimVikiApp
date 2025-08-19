package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> BaseHorizontalPager(
	list: List<T>,
	modifier: Modifier = Modifier,
	pageWidth: Dp = 260.dp,
	pagerState: PagerState,
	itemContent: @Composable (item: T, pageIndex: Int) -> Unit
) {
	HorizontalPager(
		state = pagerState,
		modifier = modifier.fillMaxWidth(),
		pageSize = PageSize.Fixed(pageWidth),
		contentPadding = PaddingValues(end = 80.dp),
		flingBehavior = PagerDefaults.flingBehavior(
			state = pagerState,
			pagerSnapDistance = PagerSnapDistance.atMost(list.size)
		),

		) { pageIndex ->
		itemContent(list[pageIndex], pageIndex)
	}
}