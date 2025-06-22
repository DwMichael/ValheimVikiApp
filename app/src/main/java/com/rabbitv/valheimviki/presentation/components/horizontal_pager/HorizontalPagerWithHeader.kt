package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader

import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

data class HorizontalPagerWithHeaderData(
	val title: String?,
	val subTitle: String?,
	val icon: ImageVector,
	val iconRotationDegrees: Float = -85f,
	val contentScale: ContentScale,
	val starLevelIndex: Int,
)

@Composable
fun <T> HorizontalPagerWithHeader(
	list: List<T>,
	headerData: HorizontalPagerWithHeaderData,
	modifier: Modifier = Modifier,
	pagerState: PagerState,
	itemContent: @Composable (item: T, pageIndex: Int) -> Unit
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.height(200.dp)
			.padding(
				start = BODY_CONTENT_PADDING.dp,
				end = BODY_CONTENT_PADDING.dp,
				bottom = BODY_CONTENT_PADDING.dp,
			),
		horizontalAlignment = Alignment.Start
	) {

		SectionHeader(
			data = headerData,
			modifier = Modifier
		)
		Spacer(modifier = Modifier.padding(6.dp))
		BaseHorizontalPager(
			list = list,
			itemContent = itemContent,
			pagerState = pagerState,
		)
	}
}