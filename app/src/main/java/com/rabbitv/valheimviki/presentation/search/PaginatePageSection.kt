package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE_SECOND
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun PaginatePageSection(
	modifier: Modifier = Modifier,
	totalPages: Int,
	currentPage: Int,
	loadNextPage: () -> Unit,
	loadPreviousPage: () -> Unit,
	loadSpecificPage: (pageNumber: Int) -> Unit,
) {

	val lazyRowState = rememberLazyListState()
	LaunchedEffect(currentPage) {
		if (totalPages > 0 && currentPage > 0) {
			val indexToScroll = (currentPage - 3).coerceAtLeast(0).coerceAtMost(totalPages - 1)
			lazyRowState.animateScrollToItem(indexToScroll)
		}
	}
	Row(
		modifier = modifier
			.fillMaxWidth()
			.height(40.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		IconButton(
			onClick = loadPreviousPage,
			enabled = currentPage > 1,
			modifier = Modifier.size(ICON_SIZE_SECOND),
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = Color.Transparent,
				contentColor = PrimaryWhite,
				disabledContentColor = PrimaryWhite.copy(alpha = 0.5f)
			)
		) {
			Icon(
				Icons.AutoMirrored.Filled.KeyboardArrowLeft,
				contentDescription = "Previous",
				modifier = Modifier.size(ICON_SIZE)
			)
		}

		LazyRow(
			modifier = Modifier
				.weight(1f)
				.height(35.dp),
			state = lazyRowState,
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
			contentPadding = PaddingValues(horizontal = 8.dp),
			userScrollEnabled = true,
		) {
			items(
				count = totalPages,
				key = { pageIndex -> pageIndex }
			) { pageIndex ->
				val pageNumber = pageIndex + 1
				val isSelected = remember(currentPage) { pageNumber == currentPage }

				TextButton(
					onClick = remember(pageNumber, currentPage) {
						{ if (pageNumber != currentPage) loadSpecificPage(pageNumber) }
					},
					colors = ButtonDefaults.textButtonColors(
						contentColor = if (isSelected) PrimaryWhite else PrimaryWhite.copy(alpha = 0.7f),
						containerColor = if (isSelected) Color(0xFF333333) else Color.Transparent
					)
				) {
					Text(
						text = pageNumber.toString(),
					)
				}
			}
		}

		IconButton(
			onClick = loadNextPage,
			enabled = currentPage < totalPages,
			modifier = Modifier.size(ICON_SIZE_SECOND),
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = Color.Transparent,
				contentColor = PrimaryWhite,
				disabledContentColor = PrimaryWhite.copy(alpha = 0.5f)
			)
		) {
			Icon(
				Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			totalPages = 10,
		)
	}
}

@Preview("PaginatePageSection - Middle Page")
@Composable
fun PreviewPaginatePageSectionMiddlePage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 6,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			totalPages = 10,
		)
	}
}

@Preview("PaginatePageSection - Last Page")
@Composable
fun PreviewPaginatePageSectionLastPage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 10,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			totalPages = 10,
		)
	}
}

@Preview("PaginatePageSection - Single Page")
@Composable
fun PreviewPaginatePageSectionSinglePage() {
	ValheimVikiAppTheme {
		PaginatePageSection(
			currentPage = 1,
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
			totalPages = 1,
		)
	}
}