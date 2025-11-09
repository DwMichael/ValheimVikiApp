package com.rabbitv.valheimviki.presentation.detail.creature.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun CardStatDetails(
	title: String,
	text: String? = null,
	icon: ImageVector,
	iconColor: Color,
	iconSize: Dp = 64.dp,
	styleTextFirst: TextStyle = MaterialTheme.typography.bodyMedium,
	styleTextSecond: TextStyle = MaterialTheme.typography.bodyLarge,
	cardPadding: Dp = 0.dp
) {
	Card(
		Modifier
			.wrapContentHeight()
			.fillMaxWidth()
			.padding(cardPadding)
			.shadow(
				elevation = 8.dp,
				shape = CardDefaults.shape,
				clip = false,
				ambientColor = Color.White.copy(alpha = 0.1f),
				spotColor = Color.White.copy(alpha = 0.25f)
			),
		colors = CardDefaults.cardColors(containerColor = LightDark),
		border = BorderStroke(2.dp, DarkWood),
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,

			) {
			Icon(
				imageVector = icon,
				tint = iconColor,
				contentDescription = stringResource(R.string.heart_icon),
				modifier = Modifier.size(iconSize)
			)
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(2.dp)
			) {
				Text(
					modifier = Modifier
						.padding(horizontal = 5.dp)
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = title.uppercase(),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = styleTextFirst,
				)
				if (text != null) {
					Text(
						modifier = Modifier
							.padding(horizontal = 5.dp)
							.wrapContentHeight(align = Alignment.CenterVertically),
						text = text.uppercase(),
						color = PrimaryWhite,
						style = styleTextSecond,
					)
				}

			}
		}
	}
}

@Preview(showBackground = true, backgroundColor = 0xFF2D2D2D)
@Composable
fun CardStatDetailsPreview() {
	ValheimVikiAppTheme {
		Box(
			Modifier
				.fillMaxWidth()
				.height(150.dp)
		)
		{
			CardStatDetails(
				title = "Health",
				text = "10000",
				icon = Lucide.Heart,
				iconColor = Color(0xFFE91E63)
			)
		}
	}
}

@Preview(showBackground = true, backgroundColor = 0xFF2D2D2D)
@Composable
fun CardStatDetailsNullTextPreview() {
	ValheimVikiAppTheme {
		Box(
			Modifier
				.fillMaxWidth()
				.height(150.dp)
		)
		{
			CardStatDetails(
				title = "Health",
				icon = Lucide.Heart,
				iconColor = Color(0xFFE91E63)
			)
		}
	}
}