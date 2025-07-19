package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.animatedSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimatedGridItem(
	item: ItemData,
	onItemClick: (itemData: ItemData) -> Unit,
	height: Dp,
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val scope = LocalSharedTransitionScope.current
		?: error("No SharedTransitionScope")
	with(scope) {
		val imageState = rememberSharedContentState("image-${item.id}")
		val surfaceState = rememberSharedContentState("surface-${item.id}")
		val textState = rememberSharedContentState("text-${item.id}")
		Box(
			modifier = Modifier
				.height(height)
				.clickable { onItemClick(item) },
			contentAlignment = Alignment.BottomStart
		) {
			AsyncImage(
				modifier = Modifier
					.sharedElement(
						sharedContentState = imageState,
						animatedVisibilityScope = animatedVisibilityScope,
						placeHolderSize = animatedSize
					)
					.fillMaxSize()
					.clip(RoundedCornerShape(MEDIUM_PADDING)),
				model = item.imageUrl,
				//TODO Check what is better for optimization
//					ImageRequest.Builder(context = LocalContext.current)
//					.data(item.imageUrl)
//					.memoryCacheKey(item.id)
//					.diskCacheKey(item.id)
//					.placeholderMemoryCacheKey(item.id)
//					.size(400)
//					.crossfade(true)
//					.scale(Scale.FILL)
//					.memoryCachePolicy(CachePolicy.ENABLED)
//					.diskCachePolicy(CachePolicy.ENABLED)
//					.build(),
				placeholder = painterResource(R.drawable.ic_placeholder),
				contentDescription = stringResource(R.string.item_grid_image),
				contentScale = ContentScale.Crop,
			)

			Surface(
				modifier = Modifier
					.sharedElement(
						sharedContentState = surfaceState,
						animatedVisibilityScope = animatedVisibilityScope,
						placeHolderSize = animatedSize
					)
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
						.sharedBounds(
							sharedContentState = textState,
							placeHolderSize = animatedSize,
							animatedVisibilityScope = animatedVisibilityScope,
						)
						.skipToLookaheadSize()
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = item.name,
					color = PrimaryWhite,
					style = MaterialTheme.typography.headlineSmall,
					overflow = TextOverflow.Ellipsis,
				)
			}
		}
	}
}


@Preview(name = "GridItem", showBackground = true)
@Composable
private fun PreviewGridItem() {

	val item = Biome(
		id = "123",
		category = "BIOME",
		imageUrl = "https://i.redd.it/l6vyacdepct71.jpg",
		name = "TestImagesasdasdasdassdas dasdasdasdasdasd",
		description = "ImageTest",
		order = 1
	)
	ValheimVikiAppTheme {
		AnimatedVisibility(true) {
			AnimatedGridItem(
				item = item,
				onItemClick = { _ -> },
				height = ITEM_HEIGHT_TWO_COLUMNS,
				animatedVisibilityScope = this,
			)
		}
	}

}