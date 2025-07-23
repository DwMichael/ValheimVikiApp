package com.rabbitv.valheimviki.presentation.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.CategoryGrid
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.presentation.material.model.MaterialUiEvent
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialCategoryScreen(
	onGridCategoryClick: () -> Unit,
	modifier: Modifier, paddingValues: PaddingValues,
	viewModel: MaterialListViewModel
) {
	val lazyGridState = rememberLazyGridState()
	val materialCategories = MaterialSegmentOption.entries

	Surface(
		color = Color.Transparent,
		modifier = Modifier
            .testTag("MaterialListSurface")
            .fillMaxSize()
            .padding(paddingValues)
	) {
		Box(
			modifier = Modifier
                .fillMaxSize()
                .padding(BODY_CONTENT_PADDING.dp)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Top
			) {
				CategoryGrid(
					modifier = modifier,
					items = materialCategories,
					onItemClick = { categorySegmentOption ->
						viewModel.onEvent(
							MaterialUiEvent.CategorySelected(
								categorySegmentOption
							)
						)
						onGridCategoryClick()
					},
					numbersOfColumns = 2,
					height = 200.dp,
					lazyGridState = lazyGridState
				)
			}
		}
	}
}


