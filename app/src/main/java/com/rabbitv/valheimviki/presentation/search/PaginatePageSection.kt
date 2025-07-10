package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch


@Composable
fun PaginatePageSection(
	modifier: Modifier = Modifier,
	pages: Int,
	currentPage: Int,
	hasMorePages: Boolean,
	loadNextPage: () -> Unit,
	loadPreviousPage: () -> Unit,
	loadSpecificPage: (pageNumber: Int) -> Unit,

	) {
	val scope = rememberCoroutineScope()
	val lazyRowState = rememberLazyListState()

	with(LocalDensity.current) { ICON_CLICK_DIM.toPx() }

	Row(
		modifier = modifier
			.fillMaxWidth()
			.wrapContentHeight(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		IconButton(
			onClick = {
				scope.launch {
					loadPreviousPage()
				}
			},
			enabled = pages > 0,
			modifier = Modifier.size(ICON_CLICK_DIM),
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = Color.Transparent,
				contentColor = PrimaryWhite,
				disabledContentColor = PrimaryWhite.copy(alpha = 0.5f)
			)
		) {
			Icon(
				Lucide.ArrowLeft,
				contentDescription = "Prev",
				modifier = Modifier.size(ICON_SIZE)
			)
		}
		LazyRow(
			modifier = Modifier.weight(1f),
			state = lazyRowState,
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
			contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp),
			userScrollEnabled = true,
		) {
			items(pages, key = { page -> page }) { page ->
				TextButton(
					onClick = {
						scope.launch {
							loadSpecificPage(page)
							lazyRowState.animateScrollToItem(page)
						}
					},
					colors = ButtonDefaults.textButtonColors(
						contentColor = if (page == currentPage) PrimaryWhite else PrimaryWhite,
						containerColor = if (page == currentPage ) Color(0xFF333333) else Color.Transparent
					),
					modifier = Modifier
						.padding(horizontal = 5.dp)
						.size(ICON_CLICK_DIM)
						.background(
							color = if (page == currentPage ) Color(0xFF333333) else Color.Transparent,
							shape = MaterialTheme.shapes.medium
						)
				) {
					Text((page + 1).toString())
				}
			}
		}


		IconButton(
			onClick = {
				scope.launch {
					loadNextPage()
				}

//				scope.launch {
//					lazyRowState.animateScrollToItem(page)
//				}
			},
			enabled = hasMorePages,
			modifier = Modifier.size(ICON_CLICK_DIM),
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = Color.Transparent,
				contentColor = PrimaryWhite,
				disabledContentColor = PrimaryWhite.copy(alpha = 0.5f)
			)
		) {
			Icon(
				Lucide.ArrowRight,
				contentDescription = "Next",
				modifier = Modifier.size(ICON_SIZE)
			)
		}
	}
}


@Preview("PaginatePageSection - First Page")
@Composable
fun PreviewPaginatePageSectionFirstPage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 1,
			hasMorePages = true,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			pages = 744
		)
	}
}

@Preview("PaginatePageSection - Middle Page")
@Composable
fun PreviewPaginatePageSectionMiddlePage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 6,
			hasMorePages = true,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			pages = 744
		)
	}
}

@Preview("PaginatePageSection - Last Page")
@Composable
fun PreviewPaginatePageSectionLastPage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 400,
			hasMorePages = false,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			pages = 744
		)
	}
}

@Preview("PaginatePageSection - Single Page")
@Composable
fun PreviewPaginatePageSectionSinglePage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 0,
			hasMorePages = false,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			pages = 1
		)
	}
}