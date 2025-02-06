package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

package com.rabbitv.valheimviki.presentation.components

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
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentGrid(
    items: List<ItemData>,
    modifier: Modifier = Modifier,
    clickToNavigate: (item: ItemData) -> Unit,
    state: PullToRefreshState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
) {
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = modifier
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
fun GridItem(
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

@Preview(name = "GridItem", showBackground = true)
@Composable
private fun PreviewListItem() {
    val item = BiomeDtoX(
        id = "123",
        stage = Stage.EARLY.toString(),
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
@Preview(name = "ContentGrid", showBackground = true)
@Composable
private fun PreviewContentList2() {

    val sampleBiomes = listOf(
        BiomeDtoX(
            id = "123123",
            name = "Forest", description = "A dense and lush forest.",

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
        ContentList(
            items = sampleBiomes,
            modifier = Modifier,
            clickToNavigate = { item -> {} },
            state = rememberPullToRefreshState(),
            onRefresh = {},
            isRefreshing = false,
        )
    }
}