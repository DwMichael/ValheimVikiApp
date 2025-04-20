package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun GreenTorchesDivider(
    height: Dp = 75.dp,
    text: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(BODY_CONTENT_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.green_thorch2),
            contentDescription = "Divider Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(45.dp) // Adjust height as needed
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            WhiteLine()
        }
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier.wrapContentWidth()
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            WhiteLine()
        }
        Image(
            painter = painterResource(id = R.drawable.green_thorch2),
            contentDescription = "Divider Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(45.dp)
                .scale(scaleX = -1f, scaleY = 1f)
        )
    }

}

@Composable
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@Preview
fun PreviewGreenTorchesDivider() {
    GreenTorchesDivider(text = "FORSAKEN POWER")
}
