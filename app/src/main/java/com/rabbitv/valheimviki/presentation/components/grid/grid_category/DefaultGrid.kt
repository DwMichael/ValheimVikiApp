package com.rabbitv.valheimviki.presentation.components.grid.grid_category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.AnimatedGridItem
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

private const val boundsAnimationDurationMillis = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultGrid(
    modifier: Modifier,
    items: List<ItemData>,
    onItemClick: (String) -> Unit,
    numbersOfColumns: Int,
    height: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    lazyGridState: LazyGridState,
) {

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(numbersOfColumns),
        horizontalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
        verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),

        ) {
        items(items, key = { item -> "${item.id}-${item.name}" }) { item ->
            Box(
                modifier = modifier.testTag("GirdItem ${item.name}")
            ) {
                AnimatedGridItem(
                    item = item,
                    onItemClick = onItemClick,
                    height = height,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
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
                height = ITEM_HEIGHT_TWO_COLUMNS,
                this,
                rememberLazyGridState()
            )
        }
    }
}