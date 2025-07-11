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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.search.model.SearchState
import com.rabbitv.valheimviki.presentation.search.viewmodel.SearchViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.toAppCategory

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
		onItemClick = onItemClick,
		onCategorySelected = { _ -> {} },
		updateQuery = viewModel::updateQuery,
		loadNextPage = viewModel::loadNextPage,
		loadPreviousPage = viewModel::loadPreviousPage,
		loadSpecificPage = viewModel::loadSpecificPage,
		uiState = uiState,
	)
}

@Composable
fun SearchScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onCategorySelected: (category: AppCategory?) -> Unit,
	updateQuery: (query: String) -> Unit,
	loadNextPage: () -> Unit,
	loadPreviousPage: () -> Unit,
	loadSpecificPage: (page: Int) -> Unit,
	uiState: SearchState,
) {
	val lazyListState = rememberLazyListState()
	val focusManager = LocalFocusManager.current
	Scaffold(

		topBar = {
			SimpleTopBar(
				modifier = Modifier,
				title = "Search",
				onClick = onBack
			)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null
				) {
					focusManager.clearFocus()
				}
		) {
			SearchTopBar(
				searchQuery = uiState.query,
				updateSearchQuery = updateQuery,
			)

			SlavicDivider()

			if (uiState.totalPages > 0 && !uiState.isLoading) {

				PaginatePageSection(
					totalPages = uiState.totalPages,
					currentPage = uiState.currentPage,
					loadNextPage = loadNextPage,
					loadPreviousPage = loadPreviousPage,
					loadSpecificPage = loadSpecificPage,
				)
			}

			Box(
				modifier = Modifier.fillMaxSize()
			) {
				if (uiState.isLoading) {
					CircularProgressIndicator(
						color = PrimaryOrange,
						modifier = Modifier.align(Alignment.Center)
					)
				} else {
					ListContent(
						items = uiState.searchList,
						clickToNavigate = { itemData ->
							val destination = NavigationHelper.routeToDetailScreen(
								itemData,
								itemData.category.toAppCategory()
							)
							onItemClick(destination)
							focusManager.clearFocus()
						},
						lazyListState = lazyListState,
						topPadding = BODY_CONTENT_PADDING.dp,
						horizontalPadding = BODY_CONTENT_PADDING.dp,
						imageScale = ContentScale.Crop,
						bottomBosPadding = 50.dp
					)
				}
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
			uiState = SearchState(),
			updateQuery = {},
			loadNextPage = {},
			loadPreviousPage = {},
			loadSpecificPage = {},
		)
	}
}