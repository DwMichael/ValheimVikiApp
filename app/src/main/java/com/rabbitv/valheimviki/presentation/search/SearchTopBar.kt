package com.rabbitv.valheimviki.presentation.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_ONE_COLUMNS
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.SecondTextWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.ApiKey


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    navigation: NavHostController
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val exampleItem: ItemData = Biome(
        id = "1",
        category = "BIOME",
        name = "Meadowns Meadowns Meadowns Meadowns",
        imageUrl = "https://img2.storyblok.com/fit-in/1920x1080/f/157036/1920x1080/cb5e84b647/valheim2.png",
        description = "SSSSS",
        order = 1
    )

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }) {
        val onActiveChange: (Boolean) -> Unit = { newActive ->
            expanded = newActive
        }
        val colors1 = SearchBarDefaults.colors()
        SearchBar(
            shape = Shapes.medium,

            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        expanded = false
                        println("Search submitted: $searchQuery")
                    },
                    expanded = expanded,
                    onExpandedChange = onActiveChange,
                    enabled = true,

                    placeholder = {
                        Text(
                            "Describe what you looking for..",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            tint = PrimaryOrange,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                expanded = !expanded
                                navigation.popBackStack()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                tint = PrimaryOrange,
                                contentDescription = "ArrowBack Icon",
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ForestGreen10Dark,
                        unfocusedContainerColor = ForestGreen10Dark,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryOrange,
                        focusedLeadingIconColor = PrimaryOrange,
                        unfocusedLeadingIconColor = PrimaryOrange,
                    ),
                    interactionSource = null,
                )
            },
            expanded = expanded,
            onExpandedChange = onActiveChange,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            colors = colors1,
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            windowInsets = SearchBarDefaults.windowInsets,
            content = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    repeat(4) { idx ->

                        SearchListItem(
                            exampleItem,
                            {},
                            ITEM_HEIGHT_ONE_COLUMNS
                        )
                    }
                }
            },
        )
    }
}


@Composable
fun SearchListItem(
    item: ItemData,
    clickToNavigate: (item: ItemData) -> Unit,
    height: Dp
) {
    val sizeResolver = rememberConstraintsSizeResolver()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data("${ApiKey.BASE_URL}{item.imageUrl}")
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
            }
            .padding(horizontal = 16.dp, vertical = 4.dp),
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 4.dp),
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
                style = MaterialTheme.typography.bodyLarge,
                color = SecondTextWhite,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding
                        (horizontal = 12.dp, vertical = 12.dp),
            )
        }
    }
}

@Preview(
    name = "SearchListItem",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSearchListItem() {
    NavHostController(LocalContext.current)

    val exampleItem: ItemData = Biome(
        id = "1",
        category = "BIOME",
        name = "Meadowns Meadowns Meadowns Meadowns",
        imageUrl = "https://img2.storyblok.com/fit-in/1920x1080/f/157036/1920x1080/cb5e84b647/valheim2.png",
        description = "SSSSS",
        order = 1
    )
    ValheimVikiAppTheme {
        "Suggestion 2"
        SearchListItem(
            exampleItem,
            {},
            ITEM_HEIGHT_ONE_COLUMNS
        )
    }
}

@Preview(
    name = "SearchTopBar",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSearchTopBar() {
    val navigation = NavHostController(LocalContext.current)
    ValheimVikiAppTheme {
        SearchTopBar(navigation)
    }
}