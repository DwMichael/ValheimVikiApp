package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkGrey
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.math.absoluteValue

@Immutable
data class HorizontalPagerData(
	val title: String,
	val subTitle: String,
	val icon: ImageVector,
	val iconRotationDegrees: Float = -85f,
	val itemContentScale: ContentScale
)


@Composable
fun HorizontalPagerSection(
	list: List<ItemData>,
	data: HorizontalPagerData,
	onItemClick: (itemId: ItemData) -> Unit = {},
	maxHeight: Dp = 240.dp,
	minHeight: Dp = 210.dp,
	pageWidth: Dp = 160.dp,
	itemHeight: Dp = 150.dp,
	itemWidth: Dp = 150.dp,
	imagePadding: Dp = 0.dp
) {
	val state = rememberPagerState(pageCount = { list.size })
	val screenWidth = LocalConfiguration.current.screenWidthDp.dp
	val horizontalPadding = remember { (screenWidth - pageWidth) / 2 }

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = minHeight, max = maxHeight)
			.padding(
				start = BODY_CONTENT_PADDING.dp,
				end = BODY_CONTENT_PADDING.dp,
				bottom = BODY_CONTENT_PADDING.dp,
			),
		horizontalAlignment = Alignment.Start
	)
	{
		HorizontalHeader(data = data)
		Spacer(modifier = Modifier.padding(6.dp))
		HorizontalPager(
			state = state,
			modifier = Modifier.fillMaxWidth(),
			contentPadding = PaddingValues(horizontal = horizontalPadding),
			pageSize = PageSize.Fixed(pageWidth),
			flingBehavior = PagerDefaults.flingBehavior(
				state = state,
				pagerSnapDistance = PagerSnapDistance.atMost(list.size)
			)
		) { pageIndex ->
			key(list[pageIndex].id) {
				HorizontalPagerItem(
					pagerState = state,
					list = list,
					pageIndex = pageIndex,
					contentScale = data.itemContentScale,
					totalSize = list.size,
					onItemClick = onItemClick,
					itemHeight = itemHeight,
					itemWidth = itemWidth,
					imagePadding = imagePadding
				)
			}
		}
	}
}

@Composable
fun HorizontalHeader(
	modifier: Modifier = Modifier,
	data: HorizontalPagerData,
) {
	Column(
		modifier = Modifier.fillMaxWidth().wrapContentHeight(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				data.icon,
				tint = Color.White,
				contentDescription = "Rectangle section Icon",
				modifier = modifier.rotate(data.iconRotationDegrees)
			)
			Spacer(modifier = Modifier.width(11.dp))
			Text(
				data.title,
				style = MaterialTheme.typography.titleLarge,
			)
		}
		if (data.subTitle.isNotBlank()) {
			Spacer(modifier = Modifier.padding(6.dp))
			Text(
				data.subTitle,
				style = MaterialTheme.typography.titleMedium,
			)
		}
	}
}

@Composable
fun HorizontalPagerItem(
	modifier: Modifier = Modifier,
	pagerState: PagerState,
	list: List<ItemData>,
	pageIndex: Int,
	totalSize: Int,
	contentScale: ContentScale,
	onItemClick: (itemId: ItemData) -> Unit,
	itemHeight: Dp = 150.dp,
	itemWidth: Dp = 150.dp,
	imagePadding: Dp
) {
	val item = list.getOrNull(pageIndex) ?: return

	Card(
		modifier = modifier
			.height(itemHeight)
			.width(itemWidth)
			.graphicsLayer {
				val pageOffset = (
						(pagerState.currentPage - pageIndex) + pagerState
							.currentPageOffsetFraction
						).absoluteValue

				val scale = lerp(
					start = 0.8f,
					stop = 1f,
					fraction = 1f - pageOffset.coerceIn(0f, 1f)
				)
				scaleX = scale
				scaleY = scale

				alpha = lerp(
					start = 0.5f,
					stop = 1f,
					fraction = 1f - pageOffset.coerceIn(0f, 1f)
				)
				cameraDistance = 8f * density
			}
			.shadow(
				elevation = 8.dp,
				shape = RoundedCornerShape(8.dp),
				spotColor = Color.White.copy(alpha = 0.25f)
			)
			.clickable { onItemClick(item) },
	) {
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			// Background Image
			AsyncImage(
				modifier = Modifier
					.fillMaxSize()
					.background(DarkGrey)
					.padding(imagePadding),
				model = ImageRequest.Builder(LocalContext.current)
					.data(item.imageUrl)
					.crossfade(true)
					.build(),
				contentDescription = stringResource(R.string.item_grid_image),
				contentScale = contentScale
			)

			// Page Counter
			Surface(
				modifier = Modifier
					.height(18.dp)
					.width(32.dp)
					.align(Alignment.TopEnd)
					.clip(RoundedCornerShape(2.dp)),
				color = ForestGreen10Dark,
			) {
				Text(
					text = "${pageIndex + 1}/$totalSize",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					color = Color.White,
					style = MaterialTheme.typography.labelSmall
				)
			}

			// Bottom Bar with Name
			Surface(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.fillMaxHeight(0.2f)
					.fillMaxWidth(),
				tonalElevation = 0.dp,
				color = Color.Black.copy(alpha = ContentAlpha.medium),
			) {
				Text(
					modifier = Modifier
						.padding(horizontal = 5.dp)
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = item.name,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = Color.White,
					style = MaterialTheme.typography.labelLarge,
				)
			}
		}
	}
}


@Composable
@Preview("HorizontalPagerItemPreview")
fun PreviewHorizontalPagerItem() {
	val pagerState = rememberPagerState(pageCount = {
		10
	})
	val creatureList = FakeData.generateFakeCreatures()
	HorizontalPagerItem(
		pagerState = pagerState,
		list = creatureList,
		pageIndex = 0,
		totalSize = 10,
		contentScale = ContentScale.Crop,
		onItemClick = {},
		itemHeight = 150.dp,
		itemWidth = 150.dp,
		imagePadding = 0.dp
	)
}

@Composable
@Preview("RectangleSectionHeader")
fun PreviewRectangleSectionHeader() {
	ValheimVikiAppTheme {
		rememberPagerState(pageCount = {
			10
		})

		val horizontalPagerData = HorizontalPagerData(
			title = "Creatuers",
			subTitle = "Creatures you may encounter in this biome",
			icon = Lucide.PawPrint,
			iconRotationDegrees = -85f,
			itemContentScale = ContentScale.Crop,
		)
		val creatureList = FakeData.generateFakeCreatures()
		HorizontalPagerSection(
			list = creatureList,
			data = horizontalPagerData,
			onItemClick = { _ -> {} },
		)
	}
}
