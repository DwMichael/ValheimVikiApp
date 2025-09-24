package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> BaseHorizontalPager(
	list: List<T>,
	modifier: Modifier = Modifier,
	pageWidth: Dp = 260.dp,
	pagerState: PagerState,
	useInfiniteScrolling: Boolean = list.size >= 3,
	itemContent: @Composable (item: T, pageIndex: Int, pagerState: PagerState) -> Unit
) {
	
	LaunchedEffect(key1 = list.size, block = {
		if (list.isNotEmpty()) {
			if (useInfiniteScrolling) {
				val startPage = Int.MAX_VALUE / 2
				val alignedPage = startPage - (startPage % list.size)
				pagerState.scrollToPage(alignedPage)
			} else {
				pagerState.scrollToPage(0)
			}
		}
	})
	
	CompositionLocalProvider(LocalOverscrollFactory provides null) {
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
			val actualIndex = if (useInfiniteScrolling) {
				pageIndex % list.size
			} else {
				pageIndex
			}
			itemContent(list[actualIndex], pageIndex, pagerState)
		}
	}
}