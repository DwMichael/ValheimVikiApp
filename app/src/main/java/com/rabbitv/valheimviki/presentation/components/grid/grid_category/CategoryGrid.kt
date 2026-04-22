package com.rabbitv.valheimviki.presentation.components.grid.grid_category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.MaterialGridItem
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun <T> CategoryGrid(
	modifier: Modifier,
	items: List<GridCategoryOption<T>>,
	onItemClick: (T) -> Unit,
	numbersOfColumns: Int,
	height: Dp,
	lazyGridState: LazyGridState,
) {
	val adaptiveInfo = LocalAdaptiveLayoutInfo.current
	val columns = adaptiveInfo.categoryGridColumns
	val itemHeight = adaptiveInfo.gridItemHeight
	val padding = adaptiveInfo.contentPadding

	LazyVerticalGrid(
		state = lazyGridState,
		columns = GridCells.Fixed(columns),
		horizontalArrangement = Arrangement.spacedBy(padding),
		verticalArrangement = Arrangement.spacedBy(padding),
		contentPadding = PaddingValues(bottom = 100.dp),
	) {
		itemsIndexed(items) { index, item ->
			MaterialGridItem(
				item = item,
				onClick = { onItemClick(item.value) },
				height = itemHeight,
			)
		}

	}
}


@Preview(name = "CategoryGrid", showBackground = true)
@Composable
private fun PreviewCategoryGrid() {
	val items = MaterialSegmentOption.entries

	ValheimVikiAppTheme {
		AnimatedVisibility(true) {
			CategoryGrid(
				modifier = Modifier,
				items = items,
				onItemClick = { _ -> {} },
				numbersOfColumns = 2,
				height = ITEM_HEIGHT_TWO_COLUMNS,
				rememberLazyGridState()
			)
		}
	}
}
