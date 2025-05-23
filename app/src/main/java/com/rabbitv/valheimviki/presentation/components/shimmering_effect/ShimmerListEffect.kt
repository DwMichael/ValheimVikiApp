package com.rabbitv.valheimviki.presentation.components.shimmering_effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DEFAULT_LIST_ITEM_HEIGHT
import com.rabbitv.valheimviki.ui.theme.DEFAULT_WITH_LIST_IMAGE
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ShimmerDarkGray

@Composable
fun ShimmerListEffect() {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
    )
    {
        items(6) {
            AnimatedShimmerListItem()
        }
    }

}

@Composable
fun AnimatedShimmerListItem() {
    val transition = rememberInfiniteTransition()
    val alphaAnim by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
    )

    ShimmerListItem(alpha = alphaAnim)

}


@Composable
fun ShimmerListItem(alpha: Float) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(DEFAULT_LIST_ITEM_HEIGHT),
        color = Color.Black,
        shape = RoundedCornerShape(size = DETAIL_ITEM_SHAPE_PADDING),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Surface(
                modifier = Modifier
                    .alpha(alpha)
                    .fillMaxHeight()
                    .width(DEFAULT_WITH_LIST_IMAGE)
                    .padding(SMALL_PADDING)
                    .clip(
                        RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING)
                    )
                    ,
                color = ShimmerDarkGray,
            ) {}
            Surface(
                modifier = Modifier
                    .alpha(alpha)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(start = 0.dp, end = SMALL_PADDING, top = SMALL_PADDING, bottom = SMALL_PADDING)
                    .clip(
                        RoundedCornerShape(
                            size = SMALL_PADDING
                        )
                    ),
                color = ShimmerDarkGray,
            ) {}
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewAnimatedShimmerListItem() {
    AnimatedShimmerListItem()
}


@Preview(showBackground = true)
@Composable
fun PreviewShimmerEffect() {
    ShimmerListEffect()
}


