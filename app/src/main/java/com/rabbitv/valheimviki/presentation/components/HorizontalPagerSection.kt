package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.detail.biome.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.math.absoluteValue

@Composable
fun HorizontalPagerSection(
    pagerState: PagerState,
    list: List<ItemData>,
    icon: ImageVector,
    title:String,
    subTitle:String,
    contentScale: ContentScale,
    iconModifier: Modifier = Modifier.rotate(-85f)
) {
    val pageWidth = 160.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - pageWidth) / 2
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
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
                        modifier = iconModifier
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    subTitle,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                pageSize = PageSize.Fixed(pageWidth),
                beyondViewportPageCount = list.size,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(list.size)
                )
            ) { pageIndex ->
                Card(
                    Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - pageIndex) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue
                            val scale = lerp(
                                start = 0.8f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleX = scale
                            scaleY = scale

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            cameraDistance = 8f * density
                        }.shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            spotColor = Color.White.copy(alpha = 0.25f)
                        )
                ) {
                    list.let {
                        Box(
                            modifier = Modifier
                                .height(150.dp),

                            ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it[pageIndex].imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.item_grid_image),
                                contentScale = contentScale
                            )
                            Surface(
                                modifier = Modifier.size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = ForestGreen10Dark,
                            ) {
                                Text(
                                    text = "${pageIndex+1}",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Surface(
                                modifier = Modifier.align(Alignment.BottomStart)
                                    .fillMaxHeight(0.2f)
                                    .fillMaxWidth(),
                                tonalElevation = 0.dp,
                                color = Color.Black.copy(alpha = ContentAlpha.medium),
                            ) {

                                Text(
                                    modifier = Modifier
                                        .padding
                                            (horizontal = 5.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    text = it[pageIndex].name.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview("RectangleSectionHeader")
fun PreviewRectangleSectionHeader() {
    ValheimVikiAppTheme {
        val pagerState = rememberPagerState(pageCount = {
            10
        })
        val creatureList = FakeData.generateFakeCreatures()
        HorizontalPagerSection(
            pagerState = pagerState,
            list = creatureList
            , Lucide.PawPrint,
            "Creatuers",
            "Creatures you may encounter in this biome",
            ContentScale.Crop,
        )
    }
}
