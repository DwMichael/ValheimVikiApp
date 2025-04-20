package com.rabbitv.valheimviki.presentation.detail.creature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkGrey
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.utils.FakeData

@Composable
fun CustomRowLayout(
    relatedSummoningItems: List<Material?>,
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier
            .padding(BODY_CONTENT_PADDING.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(relatedSummoningItems.size) { index ->
            ItemCard(
                list = relatedSummoningItems,
                pageIndex = index,
                contentScale = ContentScale.Crop,
            )
        }
    }
}


@Composable
fun ItemCard(
    list: List<ItemData?>,
    pageIndex: Int,
    contentScale: ContentScale,
) {
    Card(
        Modifier
            .size(110.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.White.copy(alpha = 0.25f)
            )
    ) {
        list.let {
            Box(
                modifier = Modifier
                    .background(DarkGrey)
                    .height(150.dp),

                ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(BODY_CONTENT_PADDING.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it[pageIndex]?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.item_grid_image),
                    contentScale = contentScale
                )
                Surface(
                    modifier = Modifier
                        .height(18.dp)
                        .width(28.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(2.dp)),
                    color = ForestGreen10Dark,
                ) {
                    Text(
                        text = "${pageIndex + 1}/${list.size}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
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
                        text = it[pageIndex]?.name.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewItemCard() {
    val fakeList = FakeData.generateFakeMaterials()
    ItemCard(
        list = fakeList,
        pageIndex = 1,
        contentScale = ContentScale.Crop,
    )
}
