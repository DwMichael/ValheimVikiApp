package com.rabbitv.valheimviki.presentation.components.grid.grid_category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.AnimatedGridItem
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultGrid(
	modifier: Modifier,
	items: List<ItemData>,
	onItemClick: (itemData: ItemData) -> Unit,
	numbersOfColumns: Int,
	height: Dp,
	animatedVisibilityScope: AnimatedVisibilityScope,
	lazyGridState: LazyGridState,
) {
	val adaptiveInfo = LocalAdaptiveLayoutInfo.current
	val columns = adaptiveInfo.gridColumns
	val itemHeight = adaptiveInfo.gridItemHeight
	val padding = adaptiveInfo.contentPadding

	LazyVerticalGrid(
		state = lazyGridState,
		columns = GridCells.Fixed(columns),
		horizontalArrangement = Arrangement.spacedBy(padding),
		verticalArrangement = Arrangement.spacedBy(padding),
		modifier = modifier.clipToBounds(),
		contentPadding = PaddingValues(bottom = 70.dp),
	) {
		items(
			items = items,
			key = { item -> item.id },
			contentType = { item -> item.category }
		) { item ->
			AnimatedGridItem(
				item = item,
				onItemClick = onItemClick,
				height = itemHeight,
				animatedVisibilityScope = animatedVisibilityScope
			)
		}

	}
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Preview(name = "ContentGrid", showBackground = true)
@Composable
private fun PreviewContentGrid() {

	val sampleBiomes = listOf(
		Biome(
			id = "123123",
			category = "BIOME",
			name = "Forest Forest", description = "A dense and lush forest.",
			imageUrl = "",
			order = 1
		),
		Biome(
			id = "123123",
			category = "BIOME",
			name = "Desert", description = "A vast and arid desert.",
			imageUrl = "",
			order = 2
		),
	)


	ValheimVikiAppTheme {
		AnimatedVisibility(true) {
			DefaultGrid(
				modifier = Modifier,
				items = sampleBiomes,
				onItemClick = { _ -> {} },
				numbersOfColumns = 2,
				height = 200.dp,
				this,
				rememberLazyGridState()
			)
		}
	}
}
