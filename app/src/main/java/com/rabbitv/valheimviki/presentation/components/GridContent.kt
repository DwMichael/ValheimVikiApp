package com.rabbitv.valheimviki.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

private const val boundsAnimationDurationMillis = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridContent(
    modifier: Modifier,
    items: List<ItemData>,
    onItemClick: (String, String) -> Unit,
    numbersOfColumns: Int,
    height: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    lazyGridState: LazyGridState,
) {

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(numbersOfColumns),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        items(items, key = { item -> item.id }) { item ->
            Box(
                modifier = modifier.testTag("GirdItem ${item.name}")
            ) {
                GridItem(
                    item = item,
                    onItemClick = onItemClick,
                    height = height,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
fun GridItem(
    item: ItemData,
    onItemClick: (String, String) -> Unit,
    height: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")


    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable {
                    onItemClick(item.id, item.name)
                },
            contentAlignment = Alignment.BottomStart
        ) {
            AsyncImage(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${item.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .fillMaxSize()
                    .clip(RoundedCornerShape(MEDIUM_PADDING)),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(item.imageUrl.toString())
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_placeholder),
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentDescription = stringResource(R.string.item_grid_image),
                contentScale = ContentScale.Crop,

                )

            Surface(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "Surface-${item.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = SMALL_PADDING,
                            bottomEnd = SMALL_PADDING
                        )
                    ),
                tonalElevation = 0.dp,
                color = Color.Black.copy(alpha = ContentAlpha.medium),
            ) {
                Text(
                    modifier = Modifier
                        .padding
                            (horizontal = 8.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "text-${item.name}"),
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 600)
                            },
//                            boundsTransform = BoundsTransform{initialBounds, targetBounds ->
//                                keyframes {
//                                    durationMillis = boundsAnimationDurationMillis
//                                    initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
//                                    targetBounds at boundsAnimationDurationMillis
//                                }
//                            },
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = item.name,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "GridItem", showBackground = true)
@Composable
private fun PreviewGridItem() {

    val item = Biome(
        id = "123",
        category = "BIOME",
        imageUrl = "https://i.redd.it/l6vyacdepct71.jpg",
        name = "TestImagesasdasdasdassdas dasdasdasdasdasd",
        description = "ImageTest",
        order = 1
    )
    ValheimVikiAppTheme {
        AnimatedVisibility(true) {
            GridItem(
                item = item,
                onItemClick = { _, _ -> },
                height = ITEM_HEIGHT_TWO_COLUMNS,
                animatedVisibilityScope = this,
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
            GridContent(
                modifier = Modifier,
                items = sampleBiomes,
                onItemClick = { _, _ -> {} },
                numbersOfColumns = 2,
                height = ITEM_HEIGHT_TWO_COLUMNS,
                this,
                rememberLazyGridState()
            )
        }
    }
}