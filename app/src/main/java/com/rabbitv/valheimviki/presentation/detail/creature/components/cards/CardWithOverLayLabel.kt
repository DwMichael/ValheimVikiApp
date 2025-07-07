package com.rabbitv.valheimviki.presentation.detail.creature.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.CustomRowLayout
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.utils.FakeData

@Composable
fun CardWithOverlayLabel(
	onClickedItem: () -> Unit = {},
	painter: Painter,
	height: Dp = 75.dp,
	content: @Composable () -> Unit = {},
	alpha: Float = 0.3f
) {
	Box(
		modifier = Modifier
			.padding(BODY_CONTENT_PADDING.dp)
			.fillMaxWidth()
			.height(height)
			.clip(RoundedCornerShape(12.dp))
			.background(Color.Transparent)
			.paint(
				painter = painter,
				contentScale = ContentScale.Crop
			)
			.clickable {
				onClickedItem()
			}
	) {
		Card(
			modifier = Modifier
				.background(Color.Black.copy(alpha = alpha))
				.fillMaxSize(),
			colors = CardDefaults.cardColors(
				containerColor = Color.Transparent
			),
			elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
		) {
			content()
		}
	}
}

@Composable
fun OverlayLabel(
	icon: ImageVector,
	label: String
) {
	Box(
		modifier = Modifier
			.clip(
				RoundedCornerShape(
					bottomEnd = DETAIL_ITEM_SHAPE_PADDING,
					topStart = DETAIL_ITEM_SHAPE_PADDING
				)
			)
			.background(Color.Black.copy(alpha = 0.4f))
			.wrapContentHeight(Alignment.Top)
			.wrapContentWidth(Alignment.Start)

	) {
		Row(
			modifier = Modifier.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				icon,
				modifier = Modifier.height(12.dp),
				tint = Color.White,
				contentDescription = "Icon Label",

				)
			Spacer(modifier = Modifier.padding(1.dp))
			Text(
				label,
				style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
				color = Color.White
			)
		}
	}
}

@Composable
@Preview(showBackground = true)
fun PreviewCardWithOverlayLabel() {
	val fakeList = FakeData.generateFakeMaterials()
	CardWithOverlayLabel(
		height = 180.dp,
		alpha = 0.1f,
		painter = painterResource(R.drawable.summoning_bg),
		content = {
			Column {
				Box(
					modifier = Modifier.fillMaxWidth()
				) {
					OverlayLabel(

						icon = Lucide.Flame,
						label = "SUMMONING ITEMS",
					)
				}
				CustomRowLayout(
					relatedSummoningItems = fakeList,
					modifier = Modifier.weight(1f)
				)
			}
		}
	)
}

