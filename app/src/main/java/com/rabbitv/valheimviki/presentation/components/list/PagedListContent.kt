package com.rabbitv.valheimviki.presentation.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.modifiers.lazyVerticalScrollbar
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.utils.toAppCategory


@Composable
fun <T : ItemData> PagedListContent(
	items: LazyPagingItems<T>,
	clickToNavigate: (itemData: ItemData) -> Unit,
	lazyListState: LazyListState,
	imageScale: ContentScale = ContentScale.Crop,
	topPadding: Dp = 0.dp,
	bottomPadding: Dp = 0.dp,
	horizontalPadding: Dp = BODY_CONTENT_PADDING.dp,
	bottomBosPadding: Dp = 24.dp,
) {
	val adaptiveInfo = LocalAdaptiveLayoutInfo.current
	val listColumns = adaptiveInfo.listColumns
	val adaptiveListItemHeight = adaptiveInfo.listItemHeight
	val adaptiveListImageWidth = adaptiveInfo.listImageWidth

	val typeOfItemList = remember {
		{ appCategory: AppCategory ->
			determineListItemType(appCategory)
		}
	}

	if (listColumns > 1) {
		val gridState = rememberLazyGridState()
		LazyVerticalGrid(
			state = gridState,
			columns = GridCells.Fixed(listColumns),
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier
				.padding(horizontal = horizontalPadding)
				.padding(top = topPadding, bottom = bottomPadding)
				.fillMaxSize(),
			contentPadding = PaddingValues(bottom = bottomBosPadding),
		) {
			items(count = items.itemCount) { index ->
				val item = items[index]
				item?.let {
					val itemType = typeOfItemList(it.category.toAppCategory())
					val scale = if (itemType == ListItemTypes.SMALL) 0.9f else 1f
					val imgScale = if (itemType == ListItemTypes.DEFAULT) imageScale else ContentScale.Fit
					ListItem(
						item = it,
						modifier = Modifier.scale(scale),
						clickToNavigate = { clickToNavigate(it) },
						imageScale = imgScale,
						itemHeight = adaptiveListItemHeight,
						imageWidth = adaptiveListImageWidth,
					)
				}
			}
		}
	} else {
		LazyColumn(
			state = lazyListState,
			modifier = Modifier
				.padding(horizontal = horizontalPadding)
				.padding(top = topPadding, bottom = bottomPadding)
				.lazyVerticalScrollbar(lazyListState)
				.fillMaxSize()
		) {
			items(count = items.itemCount) { index ->
				val item = items[index]
				item?.let {
					when (typeOfItemList(it.category.toAppCategory())) {
						ListItemTypes.SMALL -> ListItem(
							item = it,
							modifier = Modifier.scale(0.9f),
							clickToNavigate = { clickToNavigate(it) },
							imageScale = ContentScale.Fit,
							itemHeight = adaptiveListItemHeight,
							imageWidth = adaptiveListImageWidth,
						)

						ListItemTypes.MEDIUM -> ListItem(
							item = it,
							clickToNavigate = { clickToNavigate(it) },
							imageScale = ContentScale.Fit,
							itemHeight = adaptiveListItemHeight,
							imageWidth = adaptiveListImageWidth,
						)

						ListItemTypes.DEFAULT -> ListItem(
							item = it,
							clickToNavigate = { clickToNavigate(it) },
							imageScale = imageScale,
							itemHeight = adaptiveListItemHeight,
							imageWidth = adaptiveListImageWidth,
						)
					}
					Spacer(modifier = Modifier.height(16.dp))
				}
			}
			item { Box(modifier = Modifier.padding(bottomBosPadding)) }
		}
	}
}


private fun determineListItemType(appCategory: AppCategory): ListItemTypes = when (appCategory) {
	AppCategory.BIOME -> ListItemTypes.DEFAULT
	AppCategory.CREATURE -> ListItemTypes.DEFAULT
	AppCategory.FOOD -> ListItemTypes.SMALL
	AppCategory.ARMOR -> ListItemTypes.SMALL
	AppCategory.WEAPON -> ListItemTypes.SMALL
	AppCategory.BUILDING_MATERIAL -> ListItemTypes.MEDIUM
	AppCategory.MATERIAL -> ListItemTypes.SMALL
	AppCategory.CRAFTING -> ListItemTypes.MEDIUM
	AppCategory.TOOL -> ListItemTypes.SMALL
	AppCategory.MEAD -> ListItemTypes.SMALL
	AppCategory.POINTOFINTEREST -> ListItemTypes.DEFAULT
	AppCategory.TREE -> ListItemTypes.DEFAULT
	AppCategory.OREDEPOSITE -> ListItemTypes.DEFAULT
	AppCategory.TRINKETS -> ListItemTypes.DEFAULT
}
