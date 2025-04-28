package com.rabbitv.valheimviki.presentation.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun ImageWithTopLabel(
    modifier: Modifier = Modifier,
    itemData: ItemData,
    errorPainter: Painter? = null,
    horizontalDividerWidth: Dp = 100.dp ,
    subTitle: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Box(
        modifier = modifier
            .padding(BODY_CONTENT_PADDING.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black.copy(alpha = 0.25f)
            ),
        contentAlignment = Alignment.TopStart
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(itemData.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = errorPainter,
            contentDescription = stringResource(R.string.mainbosssection),
            contentScale = ContentScale.Crop,
        )
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
            tonalElevation = 0.dp,
            color = Color.Black.copy(alpha = ContentAlpha.medium),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = itemData.name,
                    color = Color.White,
                    style =textStyle,
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .padding
                            (horizontal = 8.dp),
                )
                HorizontalDivider(
                    modifier = Modifier.width(horizontalDividerWidth),
                    thickness = 1.dp,
                    color = Color.White
                )
                subTitle?.let {
                    Text(
                        text = subTitle,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.Center)
                            .padding
                                (horizontal = 8.dp),
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("ImageWithTopLabel", showBackground = false)
@Composable
fun PreviewBiomeImageHeaderSection50() {

    val fakeMainBoss = MainBoss(
        id = "boss1",
        imageUrl = "https://via.placeholder.com/400x200.png?text=MainBoss+Image",
        category = "CREATURE",
        subCategory = "BOSS",
        name = "BoneMass",
        description = "Przykładowy opis głównego bossa.",
        order = 1,
        baseHP = 1500,
        weakness = "Ogień",
        resistance = "Lód",
        baseDamage = "100",
        collapseImmune = "False",
        forsakenPower = "High"
    )
    ValheimVikiAppTheme {
        ImageWithTopLabel(
            errorPainter = painterResource(R.drawable.preview_image),
            itemData = fakeMainBoss,
        )
    }
}