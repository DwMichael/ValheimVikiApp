package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ShimmerDarkGray
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>ListContent(
    items: List<ItemData>,
    clickToNavigate: (String, T) -> Unit,
    lazyListState: LazyListState,
    subCategoryNumber: T,
    imageScale: ContentScale = ContentScale.Crop,
    horizontalPadding : Dp = BODY_CONTENT_PADDING.dp
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .padding(horizontal = horizontalPadding)
            .fillMaxSize()
    ) {
        items(items) { item ->
            ListItem(
                item = item, clickToNavigate = {
                    clickToNavigate(item.id, subCategoryNumber)
                },
                imageScale = imageScale
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(modifier = Modifier.padding(25.dp))
        }

    }
}

@Composable
fun ListItem(
    item: ItemData,
    modifier: Modifier = Modifier,
    clickToNavigate: () -> Unit,
    imageScale: ContentScale
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
            .background(ShimmerDarkGray)
            .clickable { clickToNavigate() },
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = modifier
                .fillMaxHeight()
                .width(120.dp)
                .clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING)),
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentScale = imageScale,
        )
        Text(
            text = item.name,
            color = PrimaryWhite,
            modifier = modifier
                .weight(1f)
                .fillMaxSize()
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(start = 12.dp, end = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(name = "ListItem", showBackground = true)
@Composable
private fun PreviewListItem() {
    val item = Biome(
        id = "123",
        category = "BIOME",
        imageUrl = "https://stackoverflow.com/questions/27963555/display-image-in-jsp-using-image-url",
        name = "TestImage",
        description = "ImageTest",
        order = 1
    )
    ValheimVikiAppTheme {
        ListItem(
            item = item,
            modifier = Modifier,
            clickToNavigate = {},
            imageScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "ContentList", showBackground = true)
@Composable
private fun PreviewContentList2() {

    val sampleBiomes = listOf(
        Biome(
            id = "123123",
            category = "BIOME",
            name = "Forest", description = "A dense and lush forest.",
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
        ListContent(
            items = sampleBiomes,
            clickToNavigate = { string1, intValue ->
                println("Navigating with: $string1, $intValue")
            },
            lazyListState = rememberLazyListState(),
            0,
        )
    }
}