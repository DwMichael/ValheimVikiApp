package com.rabbitv.valheimviki.presentation.components.horizontal_pager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.utils.FakeData

@Immutable
data class PagerHeaderData(
	val title: String?,
	val subTitle: String?,
	val icon: ImageVector
) {
	fun toSectionHeaderData(): SectionHeaderData =
		SectionHeaderData(title, subTitle, icon)
}

@Composable
internal fun <T : Droppable> HorizontalPagerWithHeader(
	list: List<T>,
	headerData: PagerHeaderData,
	modifier: Modifier = Modifier,
	pagerState: PagerState? = null,
	itemContent: @Composable (item: T, pageIndex: Int) -> Unit
) {
	val pagerState = pagerState ?: rememberPagerState(pageCount = { list.size })
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(
				start = BODY_CONTENT_PADDING.dp,
				end = BODY_CONTENT_PADDING.dp,
				bottom = BODY_CONTENT_PADDING.dp,
			),
		horizontalAlignment = Alignment.Start
	) {
		SectionHeader(data = headerData.toSectionHeaderData())
		Spacer(modifier = Modifier.padding(6.dp))
		BaseHorizontalPager(
			list = list,
			itemContent = itemContent,
			pagerState = pagerState,
		)
	}
}


@Preview(showBackground = true)
@Composable
fun HorizontalPagerWithHeaderPreview() {
	val sampleFoodList = listOf(
		Food(
			id = "cooked_meat",
			imageUrl = "https://picsum.photos/200/200?random=1",
			category = "Food",
			subCategory = "COOKED",
			name = "Cooked Meat",
			description = "Perfectly grilled meat that restores health and stamina.",
			order = 1,
			health = 40,
			weight = 1.0,
			healing = 2,
			stamina = 20,
			duration = "15m",
			forkType = "Normal",
			stackSize = 20
		),
		Food(
			id = "honey",
			imageUrl = "https://picsum.photos/200/200?random=2",
			category = "Food",
			subCategory = "RAW",
			name = "Honey",
			description = "Sweet golden honey that provides quick energy.",
			order = 2,
			health = 20,
			weight = 0.5,
			healing = 5,
			stamina = 35,
			duration = "20m",
			forkType = "Normal",
			stackSize = 50
		),
		Food(
			id = "bread",
			imageUrl = "https://picsum.photos/200/200?random=3",
			category = "Food",
			subCategory = "BAKED",
			name = "Bread",
			description = "Freshly baked bread that provides lasting stamina.",
			order = 3,
			health = 25,
			weight = 0.8,
			healing = 1,
			stamina = 40,
			duration = "25m",
			forkType = "Normal",
			stackSize = 10
		)
	)

	val headerData = PagerHeaderData(
		title = "Craftable Items",
		subTitle = "Items that can be created at this crafting station",
		icon = Icons.Default.Build,
	)

	val pagerState = rememberPagerState(pageCount = { sampleFoodList.size })

	HorizontalPagerWithHeader(
		list = FakeData.fakeCraftingProductsList(),
		headerData = headerData,
		pagerState = pagerState,
		modifier = Modifier.fillMaxWidth()
	) { item, pageIndex ->
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.padding(8.dp),
			elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = item.itemDrop.name,
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = item.itemDrop.name,
					style = MaterialTheme.typography.bodyMedium,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis
				)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = "Health: ${item.itemDrop.category}",
					)
				}
			}
		}
	}
}