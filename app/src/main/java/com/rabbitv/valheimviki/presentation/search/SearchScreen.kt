package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.list.PagedListContent
import com.rabbitv.valheimviki.presentation.components.topbar.SearchTopBar
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.search.model.SearchUiEvent
import com.rabbitv.valheimviki.presentation.search.viewmodel.SearchViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.flow.flowOf


@Composable
fun SearchScreen(
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SearchViewModel = hiltViewModel()
) {
	val query by viewModel.searchQuery.collectAsStateWithLifecycle()
	val pagingItems = viewModel.searchResults.collectAsLazyPagingItems()

	SearchScreenContent(
		onBack = onBack,
		onItemClick = onItemClick,
		query = query,
		pagingItems = pagingItems,
		onUpdateQuery = { viewModel.onEvent(SearchUiEvent.UpdateQuery(it)) },
	)
}

@Composable
fun SearchScreenContent(
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	query: String,
	pagingItems: LazyPagingItems<Search>,
	onUpdateQuery: (String) -> Unit,
) {
	val lazyListState = rememberLazyListState()
	val focusManager = LocalFocusManager.current
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	LaunchedEffect(pagingItems.loadState.refresh) {
		if (pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount > 0) {
			lazyListState.scrollToItem(0)
		}
	}
	Scaffold(
		topBar = {
			SimpleTopBar(title = "Search", onClick = onBack)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null
				) { focusManager.clearFocus() }
		) {
			SearchTopBar(
				searchQuery = query,
				enabled = pagingItems.itemSnapshotList.isNotEmpty(),
				updateSearchQuery = onUpdateQuery,
			)
			SlavicDivider()
			Box(Modifier.fillMaxSize()) {
				when (pagingItems.loadState.refresh) {
					is LoadState.Loading -> CircularProgressIndicator(
						color = PrimaryOrange,
						modifier = Modifier.align(Alignment.Center)
					)

					is LoadState.Error -> {
						val msg = (pagingItems.loadState.refresh as LoadState.Error)
							.error.localizedMessage ?: "Unknown error"
						Text(msg, color = Color.Red, modifier = Modifier.align(Alignment.Center))
					}

					else -> {
						PagedListContent(
							items = pagingItems,
							clickToNavigate = { item ->
								handleItemClick(item)
								focusManager.clearFocus()
							},
							lazyListState = lazyListState,
							topPadding = BODY_CONTENT_PADDING.dp,
							horizontalPadding = BODY_CONTENT_PADDING.dp,
							imageScale = ContentScale.Crop,
							bottomBosPadding = 50.dp
						)
						if (pagingItems.loadState.append is LoadState.Loading) {
							CircularProgressIndicator(
								color = PrimaryOrange,
								modifier = Modifier
									.align(Alignment.BottomCenter)
									.padding(16.dp)
							)
						}
					}
				}
			}
		}
	}
}



@Preview(
	name = "SearchScreen – paging preview",
	showBackground = true,
	backgroundColor = 0xFF202020,
)
@Composable
private fun PreviewSearchScreen() {
	ValheimVikiAppTheme {
		val mockItems = remember {
			(1..10).map { i ->
				Search(
					id          = i.toString(),
					name        = "Item $i",
					description = null,
					imageUrl    = "",
					category    = "FOOD",
					subCategory = null,
				)
			}
		}


		val lazyPagingItems = remember {
			flowOf(PagingData.from(mockItems))
		}.collectAsLazyPagingItems()


		SearchScreenContent(
			onBack        = {},
			onItemClick    = {},
			query          = "",
			pagingItems    = lazyPagingItems,
			onUpdateQuery  = {},
		)
	}
}