package com.rabbitv.valheimviki.presentation.detail.creature.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.SecondGrey

@Composable
fun CardWithImageAndTitle(
    title: String,
    imageUrl: String?,
    itemName: String,
    contentScale: ContentScale = ContentScale.Crop,
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

            Text(
                title,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Sacrifical Strones Image",
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    contentScale = contentScale
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxHeight(0.2f)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 12.dp, bottomEnd = 12.dp
                            )
                        ),
                    tonalElevation = 0.dp,
                    color = Color.Black.copy(alpha = ContentAlpha.medium),
                ) {
                    Text(
                        modifier = Modifier
                            .padding
                                (horizontal = 5.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = itemName,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

    }
}