package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun CustomItemCard(
	itemData: ItemData,
	onItemClick: (itemData: ItemData) -> Unit ={},
	fillWidth: Float = 1.0f,
	imageUrl: String,
	name: String,
	quantity: Int?
) {

	Card(
		modifier = Modifier
			.fillMaxWidth(fillWidth)
			.heightIn(160.dp)
			.shadow(
				elevation = 8.dp,
				shape = CardDefaults.shape,
				clip = false,
				ambientColor = Color.White.copy(alpha = 0.1f),
				spotColor = Color.White.copy(alpha = 0.25f)
			)
			.clickable { onItemClick(itemData) },
		colors = CardDefaults.cardColors(containerColor = LightDark),
		border = BorderStroke(2.dp, DarkWood)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize(),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AsyncImage(
				modifier = Modifier
					.fillMaxWidth()
					.height(83.dp)
					.padding(top = BODY_CONTENT_PADDING.dp),
				model = ImageRequest.Builder(LocalContext.current)
					.data(imageUrl)
					.crossfade(true)
					.build(),
				contentDescription = "Drop item",
				contentScale = ContentScale.Fit
			)
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(6.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = name,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyLarge,
					textAlign = TextAlign.Center
				)
				if (quantity != null) {
					Text(
						text = "x${quantity}",
						color = PrimaryWhite,
						style = MaterialTheme.typography.bodyLarge,
					)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomItemCard() {
	ValheimVikiAppTheme {
		CustomItemCard(
			onItemClick = {},
			fillWidth = 1.0f,
			imageUrl = "https://example.com/item.png",
			name = "Iron Sword",
			quantity = 5,
			itemData = Biome(
				id = "",
				category = "",
				subCategory = "",
				imageUrl ="",
				name = "",
				description = "",
				order = 1
			)
		)
	}
}

@Preview(showBackground = true, name = "CustomItemCard - No Quantity")
@Composable
fun PreviewCustomItemCardNoQuantity() {
	ValheimVikiAppTheme {
		CustomItemCard(
			onItemClick = {},
			fillWidth = 1.0f,
			imageUrl = "https://example.com/item.png",
			name = "Very Long Item Name That Should Be Truncated",
			quantity = null,
			itemData = Biome(
				id = "",
				category = "",
				subCategory = "",
				imageUrl ="",
				name = "",
				description = "",
				order = 1
			)
		)
	}
}

@Preview(showBackground = true, name = "CustomItemCard - Half Width")
@Composable
fun PreviewCustomItemCardHalfWidth() {
	ValheimVikiAppTheme {
		Box(modifier = Modifier.fillMaxWidth()) {
			CustomItemCard(
				onItemClick = {},
				fillWidth = 0.5f,
				imageUrl = "https://example.com/item.png",
				name = "Shield",
				quantity = 1,
				itemData = Biome(
					id = "",
					category = "",
					subCategory = "",
					imageUrl ="",
					name = "",
					description = "",
					order = 1
				)
			)
		}
	}
}
