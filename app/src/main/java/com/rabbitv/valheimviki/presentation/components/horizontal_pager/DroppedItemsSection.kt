package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.modifiers.carouselEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun DroppedItemsSection(
	modifier: Modifier = Modifier,
	onItemClick: (itemData: ItemData) -> Unit = {},
	list: List<Droppable>,
	starLevel: Int,
	icon: () -> ImageVector,
	title: String? = "Drop Items",
	subTitle: String? = "Materials that drop from creature after defeating",
) {


	val pagerState = rememberPagerState(pageCount = { list.size })

	val headerData = remember(title, subTitle, icon, starLevel) {
		PagerHeaderData(
			title = title,
			subTitle = subTitle,
			icon = icon()
		)
	}
	HorizontalPagerWithHeader(
		list = list,
		pagerState = pagerState,
		headerData = headerData,
		modifier = modifier,
	) { item, pageIndex ->
		key(item.itemDrop.id) {
			DropItemCard(
				onItemClick = { item ->
					onItemClick(item)
				},
				itemData = item.itemDrop,
				quantityList = item.quantityList,
				chanceStarList = item.chanceStarList,
				starLevel = starLevel,
				modifier = Modifier.carouselEffect(pagerState, pageIndex)
			)
		}

	}
}

@Composable
fun DropItemCard(
	modifier: Modifier = Modifier,
	onItemClick: (itemData: ItemData) -> Unit = {},
	itemData: ItemData,
	quantityList: List<Int?>,
	chanceStarList: List<Int?>,
	starLevel: Int
) {
	val quantity: Int? = quantityList[starLevel]
	Card(
		modifier = modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.shadow(
				elevation = 8.dp,
				shape = CardDefaults.shape,
				clip = false,
				ambientColor = Color.White.copy(alpha = 0.1f),
				spotColor = Color.White.copy(alpha = 0.25f)
			)
			.clickable {
				itemData.subCategory?.let { onItemClick(itemData) }
			},
		colors = CardDefaults.cardColors(containerColor = LightDark),

		border = BorderStroke(2.dp, DarkWood)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(BODY_CONTENT_PADDING.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			AsyncImage(
				modifier = Modifier.size(83.dp),
				model = ImageRequest.Builder(LocalContext.current)
					.data(itemData.imageUrl)
					.crossfade(true)
					.build(),
				contentDescription = "Drop item",
				contentScale = ContentScale.Crop
			)
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 10.dp)
			) {
				Text(
					text = itemData.name.uppercase(),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					color = PrimaryWhite,
					style = MaterialTheme.typography.labelLarge,
				)
				if (quantity != null) {
					Text(
						text = "x${quantityList[starLevel]}",
						color = PrimaryWhite,
						style = MaterialTheme.typography.labelLarge,
					)
				}
				chanceStarList.getOrNull(starLevel)?.let { chance ->
					Text(
						text = "DROP CHANCE: $chance%",
						color = PrimaryWhite,
						style = MaterialTheme.typography.labelLarge,
					)
				}
			}
		}
	}
}

@Composable
@Preview("CreatureHorizontalPager - MaterialDrop")
fun PreviewCreatureHorizontalPagerMaterial() {
	ValheimVikiAppTheme {
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

		DroppedItemsSection(
			list = materialDrops,
			starLevel = 0,
			onItemClick = { _ -> {} },
			icon = { Lucide.Trophy }
		)
	}
}


@Composable
@Preview("CreatureDropCard - Single Item")
fun PreviewCreatureDropCard() {
	ValheimVikiAppTheme {
		val material = FakeData.generateFakeMaterials().first()
		val materialDrop = MaterialDrop(
			itemDrop = material,
			quantityList = listOf(5, 10, 15),
			chanceStarList = listOf(25, 50, 75)
		)

		DropItemCard(
			itemData = materialDrop.itemDrop,
			quantityList = materialDrop.quantityList,
			chanceStarList = materialDrop.chanceStarList,
			starLevel = 1,
			onItemClick = { _ -> {} }
		)
	}
}

@Composable
@Preview("CreatureHorizontalPager - Different Star Levels")
fun PreviewCreatureHorizontalPagerStarLevels() {
	ValheimVikiAppTheme {
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

		Column {

			DroppedItemsSection(
				list = materialDrops,
				starLevel = 0,
				onItemClick = { _ -> {} },
				icon = { Lucide.Trophy }
			)

			Spacer(modifier = Modifier.height(16.dp))

			// 1 star creature
			DroppedItemsSection(
				list = materialDrops,
				starLevel = 1,
				onItemClick = { _ -> {} },
				icon = { Lucide.Trophy }
			)

			Spacer(modifier = Modifier.height(16.dp))

			// 2 star creature
			DroppedItemsSection(
				list = materialDrops,
				starLevel = 2,
				onItemClick = { _ -> {} },
				icon = { Lucide.Trophy }
			)
		}
	}
}