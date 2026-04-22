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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Wrench
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.utils.FakeData
import com.rabbitv.valheimviki.R

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
	itemContent: @Composable (item: T, pageIndex: Int, pagerState: PagerState) -> Unit
) {
	val useInfiniteScrolling = list.size >= 3
	val pageCount = if (useInfiniteScrolling) Int.MAX_VALUE else list.size
	val pagerState = rememberPagerState(
		initialPage = if (useInfiniteScrolling) Int.MAX_VALUE / 2 else 0,
		pageCount = { pageCount }
	)
	Column(
		modifier = modifier
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		SectionHeader(data = headerData.toSectionHeaderData())
		Spacer(modifier = Modifier.padding(6.dp))
		BaseHorizontalPager(
			list = list,
			itemContent = itemContent,
			pagerState = pagerState,
			useInfiniteScrolling = useInfiniteScrolling,
		)
	}
}


@Preview(showBackground = true)
@Composable
fun HorizontalPagerWithHeaderPreview() {

	val headerData = PagerHeaderData(
		title = stringResource(R.string.craftable_items),
		subTitle = "Materials that Drop from creature after defeating",
		icon = Lucide.Wrench
	)



	HorizontalPagerWithHeader(
		list = FakeData.fakeCraftingProductsList(),
		headerData = headerData,
		modifier = Modifier.fillMaxWidth()
	) { item, pageIndex, pagerState ->
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
					style = MaterialTheme.typography.headlineMedium,
				autoSize = TextAutoSize.StepBased(
					minFontSize = 18.sp,
					maxFontSize = 28.sp,
					stepSize = 1.sp,
				),
					fontWeight = FontWeight.Bold
				)
				Text(
					text = item.itemDrop.name,
					style = MaterialTheme.typography.bodyLarge,
				autoSize = TextAutoSize.StepBased(
					minFontSize = 13.sp,
					maxFontSize = 18.sp,
					stepSize = 1.sp,
				),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis
				)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.health_value, item.itemDrop.category),
					)
				}
			}
		}
	}
}