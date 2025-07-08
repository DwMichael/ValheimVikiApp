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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING

@Composable
fun FavoriteGridItem(
	item: ItemData,
	onItemClick: (String) -> Unit,
	height: Dp,
) {
	Box(
		modifier = Modifier
			.height(height)
			.clickable {
				onItemClick(item.id)
			},
		contentAlignment = Alignment.BottomStart
	) {
		AsyncImage(
			modifier = Modifier
				.fillMaxSize()
				.clip(RoundedCornerShape(MEDIUM_PADDING)),
			model = ImageRequest.Builder(context = LocalContext.current)
				.data(item.imageUrl)
				.crossfade(true)
				.build(),
			error = painterResource(R.drawable.ic_placeholder),
			placeholder = painterResource(R.drawable.ic_placeholder),
			contentDescription = stringResource(R.string.item_grid_image),
			contentScale = ContentScale.Crop,
		)

		Surface(
			modifier = Modifier
				.fillMaxHeight(0.2f)
				.fillMaxWidth()
				.clip(
					RoundedCornerShape(
						bottomStart = SMALL_PADDING,
						bottomEnd = SMALL_PADDING
					)
				),
			tonalElevation = 0.dp,
			color = Color.Black.copy(alpha = ContentAlpha.medium),
		) {
			Text(
				modifier = Modifier
					.padding
						(horizontal = 8.dp)
					.wrapContentHeight(align = Alignment.CenterVertically),
				text = item.name,
				color = PrimaryWhite,
				style = MaterialTheme.typography.titleMedium,
				overflow = TextOverflow.Ellipsis,
			)
		}
	}
}