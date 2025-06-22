package com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.math.absoluteValue

data class CreatureHorizontalPagerData(
	val title: String,
	val subTitle: String,
	val icon: ImageVector,
	val iconRotationDegrees: Float = -85f,
	val contentScale: ContentScale,
	val parentPageIndex: Int,
)

@Composable
fun <T> CreatureHorizontalPager(
	pagerState: PagerState,
	list: List<T>,
	data: CreatureHorizontalPagerData,
	getImageUrl: (T) -> String,
	getName: (T) -> String,
	getQuantity: (T, Int) -> Int?,
	getChance: (T, Int) -> Int?
) {
	val pageWidth = 300.dp

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(200.dp)
			.padding(
				start = BODY_CONTENT_PADDING.dp,
				end = BODY_CONTENT_PADDING.dp,
				bottom = BODY_CONTENT_PADDING.dp,
			)
	)
	{
		Column(
			horizontalAlignment = Alignment.Start
		)
		{
			Column(horizontalAlignment = Alignment.Start) {
				Row(
					horizontalArrangement = Arrangement.Start,
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						data.icon,
						tint = Color.White,
						contentDescription = "Rectangle section Icon",
					)
					Spacer(modifier = Modifier.width(11.dp))
					Text(
						data.title.uppercase(),
						style = MaterialTheme.typography.titleLarge,
					)
				}
				Spacer(modifier = Modifier.padding(6.dp))
				Text(
					data.subTitle,
					style = MaterialTheme.typography.titleMedium,
				)
			}
			Spacer(modifier = Modifier.padding(6.dp))
			HorizontalPager(
				state = pagerState,
				modifier = Modifier.fillMaxWidth(),
				pageSize = PageSize.Fixed(pageWidth),
				beyondViewportPageCount = list.size,
				contentPadding = PaddingValues(end = 40.dp),
				flingBehavior = PagerDefaults.flingBehavior(
					state = pagerState,
					pagerSnapDistance = PagerSnapDistance.atMost(list.size)
				)
			) { pageIndex ->
				CreatureHorizontalPagerItem(
					pagerState = pagerState,
					item = list[pageIndex],
					pageIndex = pageIndex,
					parentPageIndex = data.parentPageIndex,
					contentScale = data.contentScale,
					getImageUrl = getImageUrl,
					getName = getName,
					getQuantity = getQuantity,
					getChance = getChance,

					)
			}
		}
	}
}


@Composable
fun <T> CreatureHorizontalPagerItem(
	pagerState: PagerState,
	item: T,
	pageIndex: Int,
	parentPageIndex: Int,
	contentScale: ContentScale,
	getImageUrl: (T) -> String,
	getName: (T) -> String,
	getQuantity: (T, Int) -> Int?,
	getChance: (T, Int) -> Int?
) {
	Card(
		Modifier
			.size(280.dp)
			.graphicsLayer {
				val pageOffset =
					((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue
				val scale =
					lerp(start = 0.8f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
				scaleX = scale
				scaleY = scale
				alpha =
					lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
				cameraDistance = 8f * density
			}
			.shadow(
				elevation = 8.dp,
				shape = CardDefaults.shape,
				clip = false,
				ambientColor = Color.White.copy(alpha = 0.1f),
				spotColor = Color.White.copy(alpha = 0.25f)
			),
		colors = CardDefaults.cardColors(containerColor = LightDark),
		border = BorderStroke(2.dp, DarkWood)) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			AsyncImage(
				modifier = Modifier
					.size(83.dp)
					.background(Color.Transparent),
				model = ImageRequest.Builder(LocalContext.current)
					.data(getImageUrl(item))
					.crossfade(true)
					.build(),
				contentDescription = stringResource(R.string.item_grid_image),
				contentScale = contentScale
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
					text = getName(item).uppercase(),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyLarge,
				)
				Text(
					modifier = Modifier
						.padding(horizontal = 5.dp)
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = "x${getQuantity(item, parentPageIndex)}",
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyLarge,
				)
				Text(
					modifier = Modifier
						.padding(horizontal = 5.dp)
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = "DROP CHANCE: ${getChance(item, parentPageIndex)}%",
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
		}
	}
}

@Composable
@Preview("CreatureHorizontalPager - MaterialDrop")
fun PreviewCreatureHorizontalPagerMaterial() {
	ValheimVikiAppTheme {
		val pagerState = rememberPagerState(pageCount = { 3 })
		val materials = FakeData.generateFakeMaterials()
		val materialDrops = listOf(
			MaterialDrop(
				itemDrop = materials[0],
				quantityList = listOf(5, 10, 15),
				chanceStarList = listOf(25, 50, 75)
			),
			MaterialDrop(
				itemDrop = materials[1],
				quantityList = listOf(3, 6, 9),
				chanceStarList = listOf(20, 40, 60)
			),
			MaterialDrop(
				itemDrop = materials[2],
				quantityList = listOf(2, 4, 8),
				chanceStarList = listOf(15, 30, 45)
			)
		)

		CreatureHorizontalPager(
			pagerState = pagerState,
			list = materialDrops,
			data = CreatureHorizontalPagerData(
				title = "Drop Items",
				subTitle = "Materials that drop from creature after defeating",
				icon = Lucide.Trophy,
				iconRotationDegrees = -85f,
				contentScale = ContentScale.Crop,
				parentPageIndex = 0,
			),
			getImageUrl = { it.itemDrop.imageUrl },
			getName = { it.itemDrop.name },
			getQuantity = { item, index -> item.quantityList[index] },
			getChance = { item, index -> item.chanceStarList[index] }
		)
	}
}


@Composable
@Preview("CreatureHorizontalPagerItem - Single Item")
fun PreviewCreatureHorizontalPagerItem() {
	ValheimVikiAppTheme {
		val materials = FakeData.generateFakeMaterials()
		val materialDrop = MaterialDrop(
			itemDrop = materials[0],
			quantityList = listOf(5, 10, 15),
			chanceStarList = listOf(25, 50, 75)
		)

		CreatureHorizontalPagerItem(
			item = materialDrop,
			pageIndex = 0,
			parentPageIndex = 0,
			contentScale = ContentScale.Crop,
			getImageUrl = { it.itemDrop.imageUrl },
			getName = { it.itemDrop.name },
			getQuantity = { item, index -> item.quantityList[index] },
			getChance = { item, index -> item.chanceStarList[index] },
			pagerState = rememberPagerState(pageCount = { 1 }),
		)
	}
}

@Composable
@Preview("CreatureHorizontalPager - Different Star Levels")
fun PreviewCreatureHorizontalPagerStarLevels() {
	ValheimVikiAppTheme {
		Column {
			val materials = FakeData.generateFakeMaterials()
			val materialDrops = listOf(
				MaterialDrop(
					itemDrop = materials[0],
					quantityList = listOf(1, 3, 5),
					chanceStarList = listOf(10, 25, 50)
				)
			)

			// 1 Star
			CreatureHorizontalPager(
				pagerState = rememberPagerState(pageCount = { 1 }),
				list = materialDrops,
				data = CreatureHorizontalPagerData(
					title = "1 Star Drops",
					subTitle = "Drops from 1 star creature",
					icon = Lucide.Star,
					parentPageIndex = 0,
					contentScale = ContentScale.Crop
				),
				getImageUrl = { it.itemDrop.imageUrl },
				getName = { it.itemDrop.name },
				getQuantity = { item, index -> item.quantityList[index] },
				getChance = { item, index -> item.chanceStarList[index] }
			)

			Spacer(modifier = Modifier.height(16.dp))

			// 2 Star
			CreatureHorizontalPager(
				pagerState = rememberPagerState(pageCount = { 1 }),
				list = materialDrops,
				data = CreatureHorizontalPagerData(
					title = "2 Star Drops",
					subTitle = "Drops from 2 star creature",
					icon = Lucide.Star,
					parentPageIndex = 1,
					contentScale = ContentScale.Crop
				),
				getImageUrl = { it.itemDrop.imageUrl },
				getName = { it.itemDrop.name },
				getQuantity = { item, index -> item.quantityList[index] },
				getChance = { item, index -> item.chanceStarList[index] }
			)

			Spacer(modifier = Modifier.height(16.dp))

			// 3 Star
			CreatureHorizontalPager(
				pagerState = rememberPagerState(pageCount = { 1 }),
				list = materialDrops,
				data = CreatureHorizontalPagerData(
					title = "3 Star Drops",
					subTitle = "Drops from 3 star creature",
					icon = Lucide.Star,
					parentPageIndex = 2,
					contentScale = ContentScale.Crop
				),
				getImageUrl = { it.itemDrop.imageUrl },
				getName = { it.itemDrop.name },
				getQuantity = { item, index -> item.quantityList[index] },
				getChance = { item, index -> item.chanceStarList[index] }
			)
		}
	}
}