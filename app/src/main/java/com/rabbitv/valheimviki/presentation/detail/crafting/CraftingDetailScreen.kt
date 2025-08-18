package com.rabbitv.valheimviki.presentation.detail.crafting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedGrid
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedItems
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel.CraftingDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
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


	CraftingDetailContentV2(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = { uiState }
	)


}

@Composable
fun CraftingDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: () -> CraftingDetailUiState,
) {
	val uiState = uiState()

	val lazyListState = rememberLazyListState()
	val isExpandable = remember { mutableStateOf(false) }
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val upgHeader by remember(uiState.craftingObject?.subCategory) {
		mutableStateOf(
			when (uiState.craftingObject?.subCategory) {
				CraftingSubCategory.CRAFTING_UPGRADER.toString() ->
					SectionHeaderData(
						"Affected Crafting Station",
						"Needed for this station upgrade",
						Lucide.TrendingUp
					)

				CraftingSubCategory.CRAFTING_UPGRADER_FOOD.toString() ->
					SectionHeaderData(
						"Affected Food Station",
						"The food station improved by this item.",
						Lucide.TrendingUp
					)

				else ->
					SectionHeaderData(
						"Upgrades",
						"Improve crafting stations by building structures nearby. Higher levels unlock new recipes and allow stronger item upgrades.",
						Lucide.TrendingUp
					)
			}
		)
	}

	val lucideIcons = remember {
		listOf(
			Lucide.Utensils,
			Lucide.FlaskRound,
			Lucide.Swords,
			Lucide.Shield,
			Lucide.Cuboid,
			Lucide.OctagonAlert,
			Lucide.House,
			Lucide.Gavel
		)
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
					state = lazyListState,
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,
					contentPadding = PaddingValues(bottom = 70.dp)
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
											title = "Requirements",
											subTitle = "Components needed to build this station.",
											icon = Lucide.ScrollText
										)
									}
								}

								item(
									key = "req_grid",
									contentType = "grid"
								) {
									NestedGrid(
										nestedItems = NestedItems(items = state.data),
									) { product ->
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
									SectionHeader(
										modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
										title = upgHeader.title,
										subTitle = upgHeader.subTitle,
										icon = upgHeader.icon,
									)
								}

								item(
									key = "upg_req_grid",
									contentType = { "upg_grid" }
								) {
									NestedGrid(
										nestedItems = NestedItems(items = state.data),
									) { upg ->
										CustomItemCard(
											itemData = upg.itemDrop,
											onItemClick = handleClick,
											fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
											imageUrl = upg.itemDrop.imageUrl,
											name = upg.itemDrop.name,
											quantity = upg.quantityList.firstOrNull()
										)
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
								item(
									key = "food_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[0] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
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
								item(
									key = "mead_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[1] },
										starLevel = 0,
										title = "Mead Items",
										subTitle = "Mead items that can be created at this crafting station",
									)
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
								item(
									key = "weapon_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[2] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
								}
							}
						}
					}

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
								item(
									key = "armor_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[3] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
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
								item(
									key = "craft_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[4] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
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
								item(
									key = "fuel_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[5] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
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
								item(
									key = "bmat_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[6] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
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
								item(
									key = "tool_dropped_section",
									contentType = "dropped_section"
								) {
									DroppedItemsSection(
										onItemClick = handleClick,
										list = state.data,
										icon = { lucideIcons[7] },
										starLevel = 0,
										title = "Food Items",
										subTitle = "Food items that can be created at this crafting station",
									)
								}
							}
						}
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


private fun LazyListScope.craftingSection(
	state: UIState<List<CraftingProducts>>,
	keyPrefix: String,
	sectionContent: @Composable (List<CraftingProducts>) -> Unit
) {
	when (state) {
		is UIState.Error -> Unit
		is UIState.Loading -> {
			item(key = "${keyPrefix}_div", contentType = "divider") {
				TridentsDividedRow()
			}
			item(key = "${keyPrefix}_loading", contentType = "loading") {
				LoadingIndicator(paddingValues = PaddingValues(16.dp))
			}
		}

		is UIState.Success -> {
			if (state.data.isNotEmpty()) {
				item(key = "${keyPrefix}_div", contentType = "divider") {
					TridentsDividedRow()
				}
				item(key = "${keyPrefix}_content", contentType = "section") {
					sectionContent(state.data)
				}
			}
		}
	}
}

private fun LazyListScope.droppedItemSection(
	state: UIState<List<CraftingProducts>>,
	keyPrefix: String,
	handleClick: (ItemData) -> Unit,
	icon: ImageVector,
	title: String,
	subTitle: String
) {
	craftingSection(
		state = state,
		keyPrefix = keyPrefix
	) { data ->
		DroppedItemsSection(
			onItemClick = handleClick,
			list = data,
			icon = { icon },
			starLevel = 0,
			title = title,
			subTitle = subTitle,
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
			uiState = {
				CraftingDetailUiState(
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
			}
		)
	}
}
