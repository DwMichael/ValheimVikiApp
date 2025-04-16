package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    items: List<ItemData>,
    clickToNavigate: (item: ItemData) -> Unit,
    state: PullToRefreshState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
) {
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.background(Color.Transparent),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .absolutePadding(
                    left = 16.dp,
                    top = 0.dp,
                    right = 16.dp,
                    bottom = 16.dp
                )
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
                    ListItem(item = item, clickToNavigate = clickToNavigate)
                    HorizontalDivider()
                }
            }

        }
    }
}

@Composable
fun ListItem(
    item: ItemData,
    modifier: Modifier = Modifier,
    clickToNavigate: (item: ItemData) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                clickToNavigate(item)
            },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
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
            clickToNavigate = {}
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
            clickToNavigate = { item -> {} },
            state = rememberPullToRefreshState(),
            onRefresh = {},
            isRefreshing = false,
        )
    }
}