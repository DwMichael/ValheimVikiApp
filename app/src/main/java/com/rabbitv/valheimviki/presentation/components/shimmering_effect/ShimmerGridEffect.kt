package com.rabbitv.valheimviki.presentation.components.shimmering_effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ShimmerDarkGray

@Composable
fun ShimmerGridEffect() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    )
    {
        items(6) {
            AnimatedShimmerGridItem()
        }
    }

}

@Composable
fun AnimatedShimmerGridItem() {
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

    ShimmerGridItem(alpha = alphaAnim)

}


@Composable
fun ShimmerGridItem(alpha: Float) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(ITEM_HEIGHT_TWO_COLUMNS),
        color = Color.Black,
        shape = RoundedCornerShape(size = MEDIUM_PADDING),
    ) {
        Column(
            modifier = Modifier.padding(MEDIUM_PADDING),
            verticalArrangement = Arrangement.Bottom
        )
        {
            Surface(
                modifier = Modifier
                    .alpha(alpha)
                    .fillMaxWidth(0.5f)
                    .height(24.dp)
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

@Preview(showBackground = true)
@Composable
fun PreviewAnimatedShimmerGridItem() {
    AnimatedShimmerGridItem()
}


@Preview(showBackground = true)
@Composable
fun PreviewShimmerGridEffect() {
    ShimmerGridEffect()
}


