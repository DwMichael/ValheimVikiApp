package com.rabbitv.valheimviki.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.search.viewmodel.SearchViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun SearchScreen(
	modifier: Modifier = Modifier,
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SearchViewModel = hiltViewModel()
) {
	SearchScreenContent(
		onBack = onBack,
		onItemClick = { _ -> {} },
		onCategorySelected = { _ -> {} }
	)
}

@Composable
fun SearchScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onCategorySelected: (category: AppCategory?) -> Unit,
) {
	var searchQuery by rememberSaveable { mutableStateOf("") }
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
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)

		)
		{
			SearchTopBar(
				searchQuery = searchQuery
			)


			ListContent(
				items = emptyList(),
				clickToNavigate = { _, _ -> {} },
				lazyListState = lazyListState,
				subCategoryNumber = null,
				horizontalPadding = BODY_CONTENT_PADDING.dp,
				imageScale = ContentScale.Crop,
				bottomBosPadding = 50.dp
			)
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
		)
	}
}