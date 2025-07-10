package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.search.model.SearchState
import com.rabbitv.valheimviki.presentation.search.viewmodel.SearchViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun SearchScreen(
	modifier: Modifier = Modifier,
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SearchViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	SearchScreenContent(
		onBack = onBack,
		onItemClick = { _ -> {} },
		onCategorySelected = { _ -> {} },
		updateSearchQuery = { query -> viewModel.updateSearchQuery(query) },
		loadNextPage = { viewModel.loadNextPage() },
	loadPreviousPage = { viewModel.loadPreviousPage() },
	loadSpecificPage = { page -> viewModel.loadSpecificPage(page) },
		uiState = uiState,
	)
}

@Composable
fun SearchScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	updateSearchQuery: (query: String) -> Unit,
	onCategorySelected: (category: AppCategory?) -> Unit,
	loadNextPage: () -> Unit,
	loadPreviousPage: () -> Unit,
	loadSpecificPage: (pageNumber: Int) -> Unit,
	uiState: UiState<SearchState>,
) {

	val lazyListState = rememberLazyListState()
	Scaffold(
		topBar = {
			SimpleTopBar(
				modifier = Modifier,
				title = "Search",
				onClick = {
					onBack()
				}
			)
		},
	)
	{ innerPadding ->

		when (val state = uiState) {
			is UiState.Error -> {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = state.message ?: "An error occurred",
						style = MaterialTheme.typography.bodyLarge,
						textAlign = TextAlign.Center,
						modifier = Modifier.padding(16.dp)
					)
				}
			}

			is UiState.Loading -> {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(
						color = PrimaryOrange
					)
				}
			}

			is UiState.Success<SearchState> ->
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding)

				)
				{
					SearchTopBar(
						searchQuery = state.data.query,
						updateSearchQuery = updateSearchQuery,
					)
					SlavicDivider()
					PaginatePageSection(
						pages = state.data.results.size,
						loadNextPage = loadNextPage,
						loadPreviousPage = loadPreviousPage,
						loadSpecificPage = loadSpecificPage,
						currentPage = state.data.currentPage,
						hasMorePages = state.data.hasMorePages,
					)
					SlavicDivider()
					ListContent(
						items = state.data.results,
						clickToNavigate = { _, _ -> {} },
						lazyListState = lazyListState,
						subCategoryNumber = null,
						topPadding = BODY_CONTENT_PADDING.dp,
						horizontalPadding = BODY_CONTENT_PADDING.dp,
						imageScale = ContentScale.Crop,
						bottomBosPadding = 50.dp
					)
				}

		}

	}
}

@Preview(name = "SearchScreenContent")
@Composable
private fun PreviewSearchScreen() {
	ValheimVikiAppTheme {
		SearchScreenContent(
			onBack = {},
			onItemClick = {},
			onCategorySelected = {},
			uiState = UiState.Loading(),
			updateSearchQuery = {},
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
		)
	}
}