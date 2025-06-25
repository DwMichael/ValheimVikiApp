package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite

@Composable
fun RecipeItemCard(item: Droppable) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(150.dp)
			.shadow(
				elevation = 8.dp,
				shape = CardDefaults.shape,
				clip = false,
				ambientColor = Color.White.copy(alpha = 0.1f),
				spotColor = Color.White.copy(alpha = 0.25f)
			),
		colors = CardDefaults.cardColors(containerColor = LightDark),
		border = BorderStroke(2.dp, DarkWood)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AsyncImage(
				modifier = Modifier.size(83.dp),
				model = ImageRequest.Builder(LocalContext.current)
					.data(item.itemDrop.imageUrl)
					.crossfade(true)
					.build(),
				contentDescription = "Drop item",
				contentScale = ContentScale.Crop
			)
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(2.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = item.itemDrop.name,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyLarge,
				)
				Text(
					text = "x${item.quantityList[0]}",
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyLarge,
				)
			}
		}
	}
}