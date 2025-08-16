package com.rabbitv.valheimviki.presentation.detail.crafting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.OctagonAlert
import com.composables.icons.lucide.ScrollText
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.Utensils
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel.CraftingDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun CraftingDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: CraftingDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(CraftingDetailUiEvent.ToggleFavorite)
	}


	CraftingDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState
	)


}

@Composable
fun CraftingDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: CraftingDetailUiState,
) {
	val lazyColumnState = rememberLazyListState()
	val lazyGridState = rememberLazyGridState()

	val viewportHeight = with(LocalDensity.current) {
		lazyColumnState.layoutInfo.viewportSize.height.toDp()
	}
	val isExpandable = remember { mutableStateOf(false) }
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val gridMaxHeight = if (viewportHeight > 0.dp) viewportHeight else 1000.dp
	BgImage()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentColor = PrimaryWhite
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			uiState.craftingObject?.let { craftingObject ->
				LazyColumn(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,
				) {
					item {
						FramedImage(
							craftingObject.imageUrl,
							250.dp,
							contentScale = ContentScale.Fit
						)
					}


					item {
						Text(
							craftingObject.name,
							modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
							style = MaterialTheme.typography.displayMedium,
							textAlign = TextAlign.Center
						)

					}

					item {
						SlavicDivider()
					}


					item {
						Box(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)) {
							DetailExpandableText(
								text = craftingObject.description,
								isExpanded = isExpandable
							)
						}
					}

					when (val state = uiState.craftingMaterialToBuild) {
						is UIState.Error -> {
						}

						is UIState.Loading -> {
							item(
								key = "req_div",
								contentType = "divider"
							) {
								TridentsDividedRow()
							}
							item(
								key = "req_loading",
								contentType = "loading"
							) {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "req_div",
									contentType = "divider"
								) {
									TridentsDividedRow()
								}
								item(
									key = "req_header",
									contentType = "header"
								) {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Requirements",
												subTitle = "Components needed to build this station.",
												icon = Lucide.ScrollText,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0
											)
										)
									}
								}

								item(
									key = "req_grid",
									contentType = "grid"
								) {
									LazyVerticalGrid(
										columns = GridCells.Fixed(3),
										state = lazyGridState,
										modifier = Modifier.heightIn(max = gridMaxHeight).padding(
											MEDIUM_PADDING
										),
										userScrollEnabled = false,
										verticalArrangement = Arrangement.spacedBy(12.dp),
										horizontalArrangement = Arrangement.spacedBy(12.dp),
									) {
										itemsIndexed(
											items = state.data,
											key = { index, item -> "${item.itemDrop.id}#$index" },
											contentType = { _, _ -> "grid_item" }
										) { _, product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
									}
								}
							}
						}
					}

					when (val state = uiState.craftingUpgraderObjects) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "upg_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "upg_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "upg_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "upg_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										when (uiState.craftingObject.subCategory) {
											CraftingSubCategory.CRAFTING_UPGRADER.toString() -> SectionHeader(
												data = HorizontalPagerWithHeaderData(
													title = "Affected Crafting Station",
													subTitle = "This is the station that this item upgrades.",
													icon = Lucide.TrendingUp,
													iconRotationDegrees = 0f,
													contentScale = ContentScale.Crop,
													starLevelIndex = 0,
												)
											)

											CraftingSubCategory.CRAFTING_UPGRADER_FOOD.toString() -> SectionHeader(
												data = HorizontalPagerWithHeaderData(
													title = "Affected Food Station",
													subTitle = "The food station improved by this item.",
													icon = Lucide.TrendingUp,
													iconRotationDegrees = 0f,
													contentScale = ContentScale.Crop,
													starLevelIndex = 0,
												)
											)

											else -> SectionHeader(
												data = HorizontalPagerWithHeaderData(
													title = "Upgrades",
													subTitle = "Improve crafting stations by building structures nearby. Higher levels unlock new recipes and allow stronger item upgrades.",
													icon = Lucide.TrendingUp,
													iconRotationDegrees = 0f,
													contentScale = ContentScale.Crop,
													starLevelIndex = 0,
												)
											)
										}
									}
								}

								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString(separator = "-") { it.itemDrop.id } },
									contentType = { "upg_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { upg ->
											CustomItemCard(
												itemData = upg.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = upg.itemDrop.imageUrl,
												name = upg.itemDrop.name,
												quantity = upg.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					when (val state = uiState.craftingFoodProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "food_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "food_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "food_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "food_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Food Items",
												subTitle = "Food items that can be created at this crafting station",
												icon = Lucide.Utensils,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "food_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}

					when (val state = uiState.craftingMeadProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "mead_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "mead_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "mead_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "mead_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Mead Items",
												subTitle = "Mead items that can be created at this crafting station",
												icon = Lucide.FlaskRound,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "mead_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}

					when (val state = uiState.craftingWeaponProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(
								key = "weapon_div",
								contentType = "divider"
							) { TridentsDividedRow() }
							item(key = "weapon_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "weapon_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "weapon_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Weapon Items",
												subTitle = "Weapon items that can be created at this crafting station",
												icon = Lucide.Swords,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "weapon_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					// --- Armor Items ---
					when (val state = uiState.craftingArmorProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(
								key = "armor_div",
								contentType = "divider"
							) { TridentsDividedRow() }
							item(key = "armor_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "armor_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "armor_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Armor Items",
												subTitle = "Armor items that can be created at this crafting station",
												icon = Lucide.Shield,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "armor_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					when (val state = uiState.craftingMaterialProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(
								key = "craft_div",
								contentType = "divider"
							) { TridentsDividedRow() }
							item(key = "craft_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "craft_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "craft_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Craftable Items",
												subTitle = "Materials items that can be created at this crafting station",
												icon = Lucide.Cuboid,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "craft_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					when (val state = uiState.craftingMaterialRequired) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "fuel_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "fuel_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "fuel_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "fuel_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Fuel Requirements",
												subTitle = "This station needs at least one of the resources listed below to function",
												icon = Lucide.OctagonAlert,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "fuel_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					when (val state = uiState.craftingBuildingMaterialProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "bmat_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "bmat_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "bmat_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "bmat_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Building Materials Items",
												subTitle = "Building Materials items that can be created at this crafting station",
												icon = Lucide.House,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "bmat_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}


					when (val state = uiState.craftingToolProducts) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							item(key = "tool_div", contentType = "divider") { TridentsDividedRow() }
							item(key = "tool_loading", contentType = "loading") {
								LoadingIndicator(paddingValues = PaddingValues(16.dp))
							}
						}

						is UIState.Success -> {
							if (state.data.isNotEmpty()) {
								item(
									key = "tool_div",
									contentType = "divider"
								) { TridentsDividedRow() }
								item(key = "tool_header", contentType = "header") {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(horizontal = BODY_CONTENT_PADDING.dp)
									) {
										SectionHeader(
											data = HorizontalPagerWithHeaderData(
												title = "Tool Items",
												subTitle = "Tool items that can be created at this crafting station",
												icon = Lucide.Gavel,
												iconRotationDegrees = 0f,
												contentScale = ContentScale.Crop,
												starLevelIndex = 0,
											)
										)
									}
								}
								items(
									items = state.data.chunked(2),
									key = { row -> row.joinToString("-") { it.itemDrop.id } },
									contentType = { "tool_row" }
								) { row ->
									Row(
										Modifier
											.fillMaxWidth()
											.padding(
												horizontal = BODY_CONTENT_PADDING.dp,
												vertical = 6.dp
											)
									) {
										row.forEach { product ->
											CustomItemCard(
												itemData = product.itemDrop,
												onItemClick = handleClick,
												fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
												imageUrl = product.itemDrop.imageUrl,
												name = product.itemDrop.name,
												quantity = product.quantityList.firstOrNull()
											)
										}
										if (row.size == 1) Spacer(Modifier.weight(1f))
									}
								}
							}
						}
					}

					item {
						Spacer(
							modifier = Modifier
								.fillMaxWidth()
								.height(70.dp)
						)
					}

				}

			}
			FilledIconButton(
				onClick = onBack,
				shape = RoundedCornerShape(12.dp),
				colors = IconButtonDefaults.filledIconButtonColors(
					containerColor = ForestGreen10Dark,
				),
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(10.dp)
					.size(56.dp)
			) {
				Icon(
					Icons.AutoMirrored.Rounded.ArrowBack,
					contentDescription = "Back",
					tint = Color.White
				)
			}
			FavoriteButton(
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(16.dp),
				isFavorite = uiState.isFavorite,
				onToggleFavorite = {
					uiState.craftingObject?.let {
						onToggleFavorite()
					}
				},
			)
		}
	}
}

fun LazyGridScope.customItemCards(
	craftingProductList: List<CraftingProducts>,
	onItemClick: (ItemData) -> Unit,
) {
	items(
		items = craftingProductList,
		key = { it.itemDrop.id },
		contentType = { "grid_item" }
	) { cp ->
		CustomItemCard(
			itemData = cp.itemDrop,
			onItemClick = onItemClick,
			fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
			imageUrl = cp.itemDrop.imageUrl,
			name = cp.itemDrop.name,
			quantity = cp.quantityList.firstOrNull()
		)
	}
}


@Preview("CraftingDetailPreview", showBackground = false)
@Composable
fun PreviewCraftingDetailContent() {
	ValheimVikiAppTheme {
		CraftingDetailContent(
			onBack = {},
			onItemClick = {},
			onToggleFavorite = {},
			uiState = CraftingDetailUiState(
				craftingObject = FakeData.fakeCraftingObjectList()[0],
				craftingMaterialToBuild = UIState.Success(
					FakeData.fakeCraftingProductsList(count = 16) // > 10
				),
				craftingUpgraderObjects = UIState.Success(
					FakeData.fakeCraftingProductsList(count = 12) // też sporo
				),
				craftingFoodProducts = UIState.Loading,
				craftingMeadProducts = UIState.Loading,
				craftingMaterialProducts = UIState.Loading,
				craftingWeaponProducts = UIState.Loading,
				craftingArmorProducts = UIState.Loading,
				craftingToolProducts = UIState.Loading,
				craftingBuildingMaterialProducts = UIState.Loading
			)
		)
	}
}
