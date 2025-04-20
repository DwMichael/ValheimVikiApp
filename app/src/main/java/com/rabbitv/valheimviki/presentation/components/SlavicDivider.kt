package com.rabbitv.valheimviki.presentation.components

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun SlavicDivider() {
    val desiredHeight = 16.dp
    Box(
        modifier = Modifier.padding(vertical = BODY_CONTENT_PADDING.dp)
            .fillMaxWidth()
            .height(desiredHeight),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f).rotate(180f)) {
                SharpWhiteLine()
            }
            Image(
                painter = painterResource(id = R.drawable.divider_image),
                contentDescription = "Divider Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(desiredHeight) // Adjust height as needed
            )
            Box(modifier = Modifier.weight(1f)) {
                SharpWhiteLine()
            }
        }
    }
}

@Composable
fun SharpWhiteLine() {
    Canvas(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val path = Path()
        val startPoint = Offset(0f, size.height / 2)
        val endPoint = Offset(size.width, size.height / 2)


        val gradientBrush = Brush.linearGradient(
            colors = listOf(Color.White, Color.White.copy(alpha = 0f)),
            start = startPoint,
            end = endPoint
        )

        drawLine(
            brush = gradientBrush,
            start = startPoint,
            end = endPoint,
            strokeWidth = 2.dp.toPx()
        )
    }
}