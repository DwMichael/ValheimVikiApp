package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes

@Composable
fun FavoriteGridItem(
	imageModifier: Modifier = Modifier.fillMaxSize(),
	item: ItemData,
	onItemClick: (itemData: ItemData) -> Unit,
	height: Dp,
	contentScale: ContentScale = ContentScale.Crop,
	imageBg: @Composable () -> Unit = {}
) {

	Box(
		modifier = Modifier
			.height(height)
			.clip(Shapes.large)
			.clickable { onItemClick(item) },
		contentAlignment = Alignment.BottomStart
	) {
		imageBg()
		AsyncImage(
			modifier = imageModifier
				.clip(RoundedCornerShape(MEDIUM_PADDING)),
			model = ImageRequest.Builder(LocalContext.current)
				.data(item.imageUrl)
				.memoryCacheKey(item.id)
				.diskCacheKey(item.id)
				.size(400)
				.crossfade(true)
				.scale(Scale.FILL)
				.memoryCachePolicy(CachePolicy.ENABLED)
				.diskCachePolicy(CachePolicy.ENABLED)
				.build(),
			contentDescription = null,
			contentScale = contentScale,
		)

		Surface(
			modifier = Modifier
				.fillMaxHeight(0.2f)
				.fillMaxWidth(),
			color = Color.Black.copy(alpha = ContentAlpha.medium),
		) {
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp)
					.wrapContentHeight(align = Alignment.CenterVertically),
				text = item.name,
				color = PrimaryWhite,
				style = MaterialTheme.typography.titleMedium,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		}
	}
}