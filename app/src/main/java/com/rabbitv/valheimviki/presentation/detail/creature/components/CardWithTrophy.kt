package com.rabbitv.valheimviki.presentation.detail.creature.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.SecondGrey

@Composable
fun CardWithTrophy(
    forsakenPower: String,
    trophyUrl: String?,
    contentScale: ContentScale = ContentScale.Fit,
    height: Dp = 200.dp,
) {
    Card(
        modifier = Modifier
            .height(height)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SecondGrey
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "TROPHY",
                    style = MaterialTheme.typography.labelMedium
                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.End),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trophyUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Trophy Image",
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    contentScale = contentScale
                )
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically),
                text = forsakenPower,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }

    }
}

@Composable
@Preview("CardWithTrophy", showBackground = true)
fun PreviewCardWithTrophy() {

    CardWithTrophy(
        forsakenPower = "+300 max carry weight and +10% movement speed. +300 max carry weight and +10% movement speed.",
        trophyUrl = "sss",

        )
}
