package com.rabbitv.valheimviki.presentation.building_material

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
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialSegmentOption
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialUiEvent
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.CategoryGrid
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingMaterialCategoryScreen(
	onGridCategoryClick: () -> Unit,
	modifier: Modifier, paddingValues: PaddingValues,
	viewModel: BuildingMaterialListViewModel
) {
	val lazyGridState = rememberLazyGridState()
	val buildingMaterialCategories = BuildingMaterialSegmentOption.entries

	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("BuildingMaterialListSurface")
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
					items = buildingMaterialCategories,
					onItemClick = { categorySegmentOption ->
						viewModel.onEvent(
							BuildingMaterialUiEvent.CategorySelected(
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


