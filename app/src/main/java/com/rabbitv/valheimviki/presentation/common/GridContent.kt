package com.rabbitv.valheimviki.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.biome.Stage
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.TextWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.BASE_URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridContent(
    modifier: Modifier,
    items: List<ItemData>,
    clickToNavigate: (item: ItemData) -> Unit,
    state: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    numbersOfColumns: Int,
    height: Dp,
) {

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(numbersOfColumns),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            if (items.isEmpty()) {
                items(items) {
                    Text(
                        text = "Sprawdź połączenie z internetem",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {

                items(items) { item ->
                    println("GirdItem ${item.name}")
                    Box(
                        modifier = modifier.testTag("GirdItem ${item.name}")
                    ) {
                        GridItem(
                            item = item,
                            clickToNavigate = clickToNavigate,
                            height = height
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun GridItem(
    item: ItemData,
    clickToNavigate: (item: ItemData) -> Unit,
    height: Dp
) {
    val sizeResolver = rememberConstraintsSizeResolver()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data("$BASE_URL${item.imageUrl}")
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .size(sizeResolver)
            .build(),
    )

    Box(
        modifier = Modifier
            .height(height)
            .clickable {
                clickToNavigate(item)
            },

        contentAlignment = Alignment.BottomStart
    ) {
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(
                size = MEDIUM_PADDING
            ),
        ) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.item_grid_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .then(sizeResolver),
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = MEDIUM_PADDING,
                        bottomEnd = MEDIUM_PADDING
                    )
                ),
            tonalElevation = 0.dp,
            color = Color.Black.copy(alpha = ContentAlpha.medium),
        ) {
            Text(
                text = item.name,
                color = TextWhite,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding
                        (horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}


@Preview(name = "GridItem", showBackground = true)
@Composable
private fun PreviewGridItem() {
    val item = BiomeDtoX(
        id = "123",
        stage = Stage.EARLY.toString(),
        imageUrl = "https://http://192.168.1.130:8100/Creatures/images/creatures/bosses/Fader.png",
        name = "TestImagesasdasdasdassdas dasdasdasdasdasd",
        description = "ImageTest",
        order = 1
    )
    ValheimVikiAppTheme {
        GridItem(
            item = item,
            clickToNavigate = {},
            height = ITEM_HEIGHT_TWO_COLUMNS,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "ContentGrid", showBackground = true)
@Composable
private fun PreviewContentGrid() {

    val sampleBiomes = listOf(
        BiomeDtoX(
            id = "123123",
            name = "Forest Forest Forest", description = "A dense and lush forest.",

            stage = Stage.MID.toString(),
            imageUrl = "",
            order = 1
        ),
        BiomeDtoX(
            id = "123123",
            name = "Desert", description = "A vast and arid desert.",

            stage = Stage.EARLY.toString(),
            imageUrl = "",
            order = 2
        ),
    )


    ValheimVikiAppTheme {
        GridContent(
            modifier = Modifier,
            items = sampleBiomes,
            clickToNavigate = { item -> {} },
            state = rememberPullToRefreshState(),
            onRefresh = {},
            isRefreshing = false,
            numbersOfColumns = 2,
            height = ITEM_HEIGHT_TWO_COLUMNS
        )
    }
}