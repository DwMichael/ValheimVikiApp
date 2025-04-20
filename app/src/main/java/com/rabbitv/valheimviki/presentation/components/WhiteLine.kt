package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WhiteLine() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val startPoint = Offset(0f, size.height / 2)
        val endPoint = Offset(size.width, size.height / 2)

        drawLine(
            color = Color.White,
            start = startPoint,
            end = endPoint,
            strokeWidth = 2.dp.toPx()
        )
    }
}