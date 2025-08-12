package com.rabbitv.valheimviki.presentation.components.card.card_image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder

@Stable
@Composable
fun CardImageWithTopLabel(
	onClickedItem: (itemData: ItemData) -> Unit ={},
	modifier: Modifier = Modifier,
	itemData: ItemData,
	horizontalDividerWidth: Dp = 150.dp,
	subTitle: String? = null,
	textStyle: TextStyle = MaterialTheme.typography.titleLarge,
	contentScale: ContentScale = ContentScale.Crop,
	painter: Painter? = painterResource(id = R.drawable.bg_crafting)
) {

	val painterAsync = rememberAsyncImagePainter(itemData.imageUrl)
	val state by painterAsync.state.collectAsState()

	Card(
		modifier = modifier
			.padding(BODY_CONTENT_PADDING.dp)
			.height(300.dp)
			.clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
			.border(1.dp, YellowDTBorder, RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
			.shadow(
				elevation = 8.dp,
				shape = RoundedCornerShape(8.dp),
				spotColor = Color.Black.copy(alpha = 0.25f)
			)
			.clickable { onClickedItem(itemData) },
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent,
			contentColor = Color.Transparent,
			disabledContainerColor = Color.Transparent,
			disabledContentColor = Color.Transparent,
		)
	) {
		Column {
			Surface(
				modifier = Modifier
					.fillMaxHeight(0.25f)
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
						style = textStyle,
						modifier = Modifier
							.wrapContentSize(align = Alignment.Center)
							.padding(horizontal = 8.dp),
						textAlign = TextAlign.Center
					)
					HorizontalDivider(
						modifier = Modifier
							.width(horizontalDividerWidth)
							.padding(5.dp),
						thickness = 1.dp,
						color = Color.White
					)
					subTitle?.let {
						Text(
							text = subTitle,
							color = Color.White,
							style = MaterialTheme.typography.labelLarge,
							modifier = Modifier
								.wrapContentSize(align = Alignment.Center)
								.padding(horizontal = 8.dp),
							textAlign = TextAlign.Center
						)
					}
				}
			}
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.Transparent)
			) {
				painter?.let {
					Image(
						modifier = Modifier.fillMaxSize(),
						painter = it,
						contentDescription = "Bg_image",
						contentScale = ContentScale.Crop
					)
				}
				when (state) {
					is AsyncImagePainter.State.Empty,
					is AsyncImagePainter.State.Loading -> {
						CircularProgressIndicator()
					}

					is AsyncImagePainter.State.Success -> {
						Image(
							modifier = Modifier.fillMaxSize(),
							painter = painterAsync,
							contentDescription = "Card Image",
							contentScale = contentScale
						)
					}

					is AsyncImagePainter.State.Error -> {

					}
				}

			}
		}
	}
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("CardImageWithTopLabel", showBackground = false)
@Composable
fun PreviewCardImageWithTopLabel() {

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
		CardImageWithTopLabel(
			itemData = fakeMainBoss,
			subTitle = "Przykładowy opis głównego bossa Przykładowy.",
			contentScale = ContentScale.FillBounds
		)
	}
}