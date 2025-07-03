package com.rabbitv.valheimviki.presentation.detail.crafting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.BookOpenCheck
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
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.custom_column_grid.CustomColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel.CraftingDetailViewModel
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

//TODO OPTIMALIZE IT

@Composable
fun CraftingDetailScreen(
	onBack: () -> Unit,
	viewmodel: CraftingDetailViewModel = hiltViewModel()
) {
	val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

	CraftingDetailContent(
		onBack = onBack,
		uiState = uiState
	)


}

@Composable
fun CraftingDetailContent(
	onBack: () -> Unit,
	uiState: CraftingDetailUiState,
) {


	val scrollState = rememberLazyListState()
	val isExpandable = remember { mutableStateOf(false) }
	val previousScrollValue = remember { mutableIntStateOf(0) }
	val backButtonVisibleState by remember {
		derivedStateOf {
			val currentScroll = scrollState.firstVisibleItemScrollOffset
			val previous = previousScrollValue.intValue
			val isVisible = when {
				currentScroll == 0 -> true
				currentScroll < previous -> true
				currentScroll > previous -> false
				else -> true
			}
			previousScrollValue.intValue = currentScroll
			isVisible
		}
	}


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
					modifier = Modifier
						.fillMaxSize()
						.padding(
							top = 20.dp,
						),
					state = scrollState,
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,
					overscrollEffect = rememberOverscrollEffect()
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
					item {
						if (uiState.craftingMaterialToBuild.isNotEmpty()) {
							TridentsDividedRow()
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
										starLevelIndex = 0,
									),
									modifier = Modifier,
								)
							}

							Spacer(modifier = Modifier.padding(6.dp))
							TwoColumnGrid {
								for (craftingMaterial in uiState.craftingMaterialToBuild) {
									CustomItemCard(
										fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
										imageUrl = craftingMaterial.itemDrop.imageUrl,
										name = craftingMaterial.itemDrop.name,
										quantity = craftingMaterial.quantityList.firstOrNull()
									)
								}
							}
						}
					}
					item {
						if (uiState.craftingUpgraderObjects.isNotEmpty()) {
							TridentsDividedRow()
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
										),
										modifier = Modifier,
									)

									CraftingSubCategory.CRAFTING_UPGRADER_FOOD.toString() -> SectionHeader(
										data = HorizontalPagerWithHeaderData(
											title = "Affected Food Station",
											subTitle = "The food station improved by this item.",
											icon = Lucide.TrendingUp,
											iconRotationDegrees = 0f,
											contentScale = ContentScale.Crop,
											starLevelIndex = 0,
										),
										modifier = Modifier,
									)

									else -> SectionHeader(
										data = HorizontalPagerWithHeaderData(
											title = "Upgrades",
											subTitle = "Improve crafting stations by building structures nearby. Higher levels unlock new recipes and allow stronger item upgrades.",
											icon = Lucide.TrendingUp,
											iconRotationDegrees = 0f,
											contentScale = ContentScale.Crop,
											starLevelIndex = 0,
										),
										modifier = Modifier,
									)
								}

							}

							Spacer(modifier = Modifier.padding(6.dp))
							TwoColumnGrid {
								for (craftingUpgrader in uiState.craftingUpgraderObjects) {
									CustomItemCard(
										fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
										imageUrl = craftingUpgrader.itemDrop.imageUrl,
										name = craftingUpgrader.itemDrop.name,
										quantity = craftingUpgrader.quantityList.firstOrNull()
									)
								}
							}
						}
					}
					item {
						if (uiState.craftingFoodProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingFoodProducts,
								icon = Lucide.Utensils,
								starLevel = 0,
								title = "Food Items",
								subTitle = "Food items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingMeadProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingMeadProducts,
								icon = Lucide.BookOpenCheck,
								starLevel = 0,
								title = "Crafting Ingredients",
								subTitle = "Materials this station consumes to produce items.",
							)
						}
					}
					item {
						if (uiState.craftingMeadProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingMeadProducts,
								icon = Lucide.FlaskRound,
								starLevel = 0,
								title = "Mead Items",
								subTitle = "Mead items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingWeaponProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingWeaponProducts,
								icon = Lucide.Swords,
								starLevel = 0,
								title = "Weapon Items",
								subTitle = "Weapon items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingArmorProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingArmorProducts,
								icon = Lucide.Shield,
								starLevel = 0,
								title = "Armor Items",
								subTitle = "Armor items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingMaterialProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingMaterialProducts,
								icon = Lucide.Cuboid,
								starLevel = 0,
								title = "Craftable Items",
								subTitle = "Materials items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingMaterialRequired.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingMaterialRequired,
								icon = Lucide.OctagonAlert,
								starLevel = 0,
								title = "Fuel Requirements",
								subTitle = "This station needs at least one of the resources listed below to function",
							)
						}
					}

					item {
						if (uiState.craftingBuildingMaterialProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingBuildingMaterialProducts,
								icon = Lucide.House,
								starLevel = 0,
								title = "Building Materials Items",
								subTitle = "Building Materials items that can be created at this crafting station",
							)
						}
					}
					item {
						if (uiState.craftingToolProducts.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								list = uiState.craftingToolProducts,
								icon = Lucide.Gavel,
								starLevel = 0,
								title = "Tool Items",
								subTitle = "Tool items that can be created at this crafting station",
							)
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
			AnimatedVisibility(
				visible = backButtonVisibleState,
				enter = fadeIn(),
				exit = fadeOut(),
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp)
			) {
				FilledIconButton(
					onClick = onBack,
					shape = RoundedCornerShape(12.dp),
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = ForestGreen10Dark,
					),
					modifier = Modifier.size(56.dp)
				) {
					Icon(
						Icons.AutoMirrored.Rounded.ArrowBack,
						contentDescription = "Back",
						tint = Color.White
					)
				}
			}
		}
	}
}


@Preview("CraftingDetailPreview", showBackground = false)
@Composable
fun PreviewCraftingDetailContent() {
	ValheimVikiAppTheme {
		CraftingDetailContent(
			onBack = {},
			uiState = CraftingDetailUiState(
				craftingObject = FakeData.fakeCraftingObjectList()[0],
				craftingUpgraderObjects = FakeData.fakeCraftingProductsList(),
				craftingFoodProducts = FakeData.fakeCraftingProductsList(),
				craftingMeadProducts = FakeData.fakeCraftingProductsList(),
				craftingMaterialProducts = FakeData.fakeCraftingProductsList(),
				craftingWeaponProducts = FakeData.fakeCraftingProductsList(),
				craftingArmorProducts = FakeData.fakeCraftingProductsList(),
				craftingToolProducts = FakeData.fakeCraftingProductsList(),
				craftingBuildingMaterialProducts = FakeData.fakeCraftingProductsList(),
				isLoading = false,
				error = null
			)
		)
	}
}