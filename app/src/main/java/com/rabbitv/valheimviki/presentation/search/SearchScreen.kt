package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.topbar.SearchTopBar
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.search.model.SearchUiEvent
import com.rabbitv.valheimviki.presentation.search.viewmodel.SearchViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun SearchScreen(
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SearchViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	SearchScreenContent(
		onBack = onBack,
		onItemClick = onItemClick,
		query = uiState.query,
		totalPages = uiState.totalPages,
		searchList = uiState.searchList,
		currentPage = uiState.currentPage,
		onEvent = viewModel::onEvent,
	)
}

@Composable
fun SearchScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	query: String,
	totalPages: Int,
	searchList: UIState<List<Search>>,
	currentPage: Int,
	onEvent: (event: SearchUiEvent) -> Unit,
) {
	val lazyListState = rememberLazyListState()
	val focusManager = LocalFocusManager.current
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

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
				searchQuery = query,
				updateSearchQuery = { updateQuery -> onEvent(SearchUiEvent.UpdateQuery(updateQuery)) },
			)

			SlavicDivider()

			SearchPaginationAndList(
				totalPages = totalPages,
				currentPage = currentPage,
				searchList = searchList,
				onEvent = onEvent,
				handleItemClick = handleItemClick,
				focusManager = focusManager,
				lazyListState = lazyListState
			)
		}
	}
}

@Composable
private fun SearchPaginationAndList(
	totalPages: Int,
	currentPage: Int,
	searchList: UIState<List<Search>>,
	onEvent: (event: SearchUiEvent) -> Unit,
	handleItemClick: (ItemData) -> Unit,
	focusManager: FocusManager,
	lazyListState: LazyListState
) {
	val isLoading = searchList is UIState.Loading
	val isError = searchList is UIState.Error
	val isSuccess = searchList is UIState.Success
	val searchListData = if (searchList is UIState.Success) searchList.data else emptyList()

	if (totalPages > 0 && isSuccess) {
		PaginatePageSection(
			totalPages = totalPages,
			currentPage = currentPage,
			loadNextPage = { onEvent(SearchUiEvent.LoadNextPage) },
			loadPreviousPage = { onEvent(SearchUiEvent.LoadPreviousPage) },
			loadSpecificPage = { page -> onEvent(SearchUiEvent.LoadSpecificPage(page)) },
		)
	}

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		when {
			isLoading -> {
				CircularProgressIndicator(
					color = PrimaryOrange,
					modifier = Modifier.align(Alignment.Center)
				)
			}

			isError -> {
				val errorMsg = searchList.message
				Text(
					text = errorMsg,
					color = Color.Red,
					modifier = Modifier.align(Alignment.Center)
				)
			}

			isSuccess -> {
				ListContent(
					items = searchListData,
					clickToNavigate = { itemData ->
						handleItemClick(itemData)
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

@Preview(name = "SearchScreenContent")
@Composable
private fun PreviewSearchScreen() {
	ValheimVikiAppTheme {
		SearchScreenContent(
			onBack = {},
			onItemClick = {},
			query = "",
			totalPages = 0,
			searchList = UIState.Success(emptyList()),
			currentPage = 0,
			onEvent = {},
		)
	}
}