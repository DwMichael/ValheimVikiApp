package com.rabbitv.valheimviki.presentation.components.main_detail_image

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.animatedSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.generateFakeMaterials


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainDetailImageAnimated(
    onBack: () -> Unit = {},
    id: String,
    imageUrl: String,
    title: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    textAlign: TextAlign = TextAlign.Center,
) {
    val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
    BackHandler(enabled = isRunning) {
    }
    Box(modifier = Modifier.fillMaxSize()) {
        with(sharedTransitionScope) {
            val imageState = rememberSharedContentState("image-$id")
            val surfaceState = rememberSharedContentState("surface-$id")
            val textState = rememberSharedContentState("text-$id")

            Surface(
                shape = RoundedCornerShape(MEDIUM_PADDING),
                modifier = Modifier
                    .heightIn(min = 200.dp, max = 320.dp)
                    .fillMaxWidth()
                    .sharedElement(
                        imageState,
                        animatedVisibilityScope,
                        placeHolderSize = animatedSize
                    )
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .placeholderMemoryCacheKey("image--$id")
                            .memoryCacheKey("image--$id")
                            .crossfade(!isRunning)
                            .build(),
                        contentDescription = stringResource(R.string.item_grid_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .fillMaxHeight(0.22f)
                            .sharedElement(
                                surfaceState,
                                animatedVisibilityScope, placeHolderSize = animatedSize,
                            )

                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = textAlign,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .sharedBounds(
                                    textState,
                                    animatedVisibilityScope, placeHolderSize = animatedSize,
                                )

                                .wrapContentHeight(Alignment.CenterVertically)

                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = !isRunning,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            FilledIconButton(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = ForestGreen10Dark,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewMainDetailImageAnimated() {
    ValheimVikiAppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                val itemsData = generateFakeMaterials()
                MainDetailImageAnimated(
                    onBack = {},


                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    id = itemsData.first().id,
                    imageUrl = itemsData.first().imageUrl,
                    title = itemsData.first().name,

                    )
            }
        }
    }
}