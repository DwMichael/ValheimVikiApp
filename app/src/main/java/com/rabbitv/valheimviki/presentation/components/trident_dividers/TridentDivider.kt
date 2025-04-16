package com.rabbitv.valheimviki.presentation.components.trident_dividers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ThirdGrey

@Composable
fun TridentDivider(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .height(24.dp)
            .fillMaxWidth()

    ) {
        val centerY = size.height / 2
        val centerX = size.width / 2
        // Draw Trident on Left Side
        drawArc(
            color = ThirdGrey,
            startAngle = 90f,    // start at the top
            sweepAngle = 180f,   // sweep half-circle down to the bottom
            useCenter = false,   // don't connect the arc's ends to the center
            topLeft = Offset(0f, 20f),
            size = Size(20f, size.height - 40),
            style = Stroke(width = 4f, cap = StrokeCap.Round) // rounded endpoints
        )
        val offsetX = -20f
        val path = Path().apply {
            moveTo(10f, centerY)  // Center of trident base

            // Left prong
            lineTo(-35f, centerY)
            lineTo(centerX, centerY)

            //first strait line
            moveTo(-5f, centerY - 10f)
            lineTo(-5f, centerY + 10f)

            //second strait line big
            moveTo(-15f + offsetX, centerY - 12f)

            // Linia górna (do prawego-górnego narożnika)
            lineTo(10f + offsetX, centerY - 12f)

            // Linia prawa (do prawego-dolnego narożnika)
            lineTo(10f + offsetX, centerY + 12f)

            // Linia dolna (wracamy w lewo)
            lineTo(-15f + offsetX, centerY + 12f)

            //third strait line
            moveTo(-18f, centerY - 8f)
            lineTo(-18f, centerY + 8f)
            //last strait line
            moveTo(-26f, centerY - 8f)
            lineTo(-26f, centerY + 8f)

        }


        drawPath(
            path = path,
            color = ThirdGrey,
            style = Stroke(width = 4f)
        )

        // Draw Horizontal Line extending from the trident
        drawLine(
            color = ThirdGrey,
            start = Offset(22f, centerY),
            end = Offset(size.width, centerY),
            strokeWidth = 4f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTridentDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TridentDivider()
    }
}
