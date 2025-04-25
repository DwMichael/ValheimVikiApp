package com.rabbitv.valheimviki.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.generateFakeMaterials

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainDetailImage(
    onBack: () -> Unit = {},
    itemData: ItemData,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    textAlign: TextAlign = TextAlign.Start,
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .heightIn(min = 200.dp, max = 320.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            AsyncImage(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${itemData.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 600)
                        }
                    )
                    .fillMaxSize()
                    .clickable {
                        onBack()
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(itemData.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.item_grid_image),
                contentScale = ContentScale.Crop,
            )
            Surface(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "Surface-${itemData.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 600)
                        }
                    )
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(),
                tonalElevation = 0.dp,
                color = Color.Black.copy(alpha = ContentAlpha.medium),
            ) {

                Text(
                    modifier = Modifier
                        .padding
                            (horizontal = 8.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "text-${itemData.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 600)
                            }
                        ).wrapContentHeight(Alignment.CenterVertically),
                    textAlign = textAlign,
                    text = itemData.name,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                )

            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewMainDetailImage(){
    ValheimVikiAppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                val itemsData = generateFakeMaterials()
                MainDetailImage(
                    onBack = {},
                    itemData = itemsData.first(),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}