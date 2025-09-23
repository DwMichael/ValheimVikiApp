package com.rabbitv.valheimviki.presentation.components.grid.nested

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING

@Stable
data class NestedItems<T>(
	val items: List<T>
)

@Composable
fun <T : Droppable> NestedGrid(
	nestedItems: NestedItems<T>,
	gridCells: Int = 3,
	gridMaxHeight: Dp = 1000.dp,
    horizontalPadding: Dp = MEDIUM_PADDING,
	gridItem: @Composable (T) -> Unit
) {
	val lazyGridState = rememberLazyGridState()

	LazyVerticalGrid(
		columns = GridCells.Fixed(gridCells),
		state = lazyGridState,
		modifier = Modifier
			.heightIn(max = gridMaxHeight)
			.padding(
                horizontal = horizontalPadding,
                vertical = MEDIUM_PADDING
			),
		userScrollEnabled = false,
		verticalArrangement = Arrangement.spacedBy(12.dp),
		horizontalArrangement = Arrangement.spacedBy(12.dp),
	) {
		itemsIndexed(
			items = nestedItems.items,
			key = { index, item -> "${item.itemDrop.id}#$index" },
			contentType = { _, _ -> "grid_item" }
		) { _, product ->
			gridItem(product)
		}
	}
}