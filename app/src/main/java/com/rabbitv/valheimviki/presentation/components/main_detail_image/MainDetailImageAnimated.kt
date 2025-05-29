package com.rabbitv.valheimviki.presentation.components.main_detail_image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.generateFakeMaterials

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainDetailImageAnimated(
    onBack: () -> Unit = {},
    itemData: ItemData,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    textAlign: TextAlign = TextAlign.Start,
    painter: AsyncImagePainter
) {
    val backButtonVisibleState = remember { mutableStateOf(false) }
    LaunchedEffect(animatedVisibilityScope.transition.isRunning) {
        backButtonVisibleState.value = !animatedVisibilityScope.transition.isRunning
    }

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .heightIn(min = 200.dp, max = 320.dp),
        ) {
            Image(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "image-${itemData.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> tween(durationMillis = 600) },
                    )
                    .fillMaxSize(),
                painter = painter,
                contentDescription = stringResource(R.string.item_grid_image),
                contentScale = ContentScale.Crop,
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "Surface-${itemData.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> tween(durationMillis = 600) },
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
                            sharedContentState = rememberSharedContentState(key = "text-${itemData.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 600)
                            },
                        )
                        .skipToLookaheadSize()
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = textAlign,
                    text = itemData.name,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                )

            }
            AnimatedVisibility(
                visible = backButtonVisibleState.value,
                enter = fadeIn(animationSpec = tween(300))
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.TopStart,
                ) {
                    FilledIconButton(
                        onClick = { onBack() },
                        shape = RoundedCornerShape(BODY_CONTENT_PADDING.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = ForestGreen10Dark,
                        ),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            tint = PrimaryWhite,
                            contentDescription = "Navigation Back Button"
                        )
                    }
                }
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
                    itemData = itemsData.first(),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    painter = rememberAsyncImagePainter(""),
                )
            }
        }
    }
}