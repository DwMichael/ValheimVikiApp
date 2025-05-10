package com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.detail.creature.components.DropItem
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.math.absoluteValue

@Composable
fun CreatureHorizontalPager(
    pagerState: PagerState,
    list: List<DropItem>,
    icon: ImageVector,
    title: String,
    subTitle: String,
    contentScale: ContentScale,
    parentPageIndex: Int,
) {
    val pageWidth = 300.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(
                start = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp,
                bottom = BODY_CONTENT_PADDING.dp,
            )
    )
    {
        Column(
            horizontalAlignment = Alignment.Start
        )
        {
            Column(horizontalAlignment = Alignment.Start) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        tint = Color.White,
                        contentDescription = "Rectangle section Icon",
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        title.uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    subTitle,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSize = PageSize.Fixed(pageWidth),
                beyondViewportPageCount = list.size,
                contentPadding = PaddingValues(end = 40.dp),
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(list.size)
                )
            ) { pageIndex ->
                CreatureHorizontalPagerItem(
                    parentPageIndex = parentPageIndex,
                    pagerState = pagerState,
                    list = list,
                    pageIndex = pageIndex,
                    contentScale = contentScale,
                )
            }
        }
    }
}


@Composable
fun CreatureHorizontalPagerItem(
    pagerState: PagerState,
    list: List<DropItem>,
    pageIndex: Int,
    parentPageIndex: Int,
    contentScale: ContentScale,
) {
    list.let {
        Card(
            Modifier
                .size(280.dp)
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue
                    val scale =
                        lerp(start = 0.8f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
                    scaleX = scale
                    scaleY = scale
                    alpha =
                        lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
                    cameraDistance = 8f * density
                }
                .shadow(
                    elevation = 8.dp,
                    shape = CardDefaults.shape,
                    clip = false,
                    ambientColor = Color.White.copy(alpha = 0.1f),
                    spotColor = Color.White.copy(alpha = 0.25f)
                ),
            colors = CardDefaults.cardColors(containerColor = LightDark),
            border = BorderStroke(2.dp, DarkWood)) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(83.dp)
                        .background(Color.Transparent),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it[pageIndex].material.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.item_grid_image),
                    contentScale = contentScale
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = it[pageIndex].material.name.toString().uppercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "x${it[pageIndex].quantityList[parentPageIndex].toString()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "DROP CHANCE: ${
                            it[pageIndex].chanceStarList[parentPageIndex].toString()
                        }%",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
@Preview("CreatureHorizontalPagerItem")
fun PreviewCreatureHorizontalPagerItem() {
    ValheimVikiAppTheme {
        val pagerState = rememberPagerState(pageCount = {
            1
        })
        val materials = FakeData.generateFakeMaterials()
        val list1 = listOf(1, 1, 1)
        val list2 = listOf(2, 2, 2)
        listOf(
            DropItem(
                material = materials[0],
                quantityList = list1,
                chanceStarList = list2
            )

        ).also {
            CreatureHorizontalPagerItem(
                pagerState = pagerState,
                list = it,
                pageIndex = 0,
                parentPageIndex = 0,
                contentScale = ContentScale.Crop
            )
        }
    }
}