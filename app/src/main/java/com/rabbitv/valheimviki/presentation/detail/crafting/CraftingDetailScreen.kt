package com.rabbitv.valheimviki.presentation.detail.crafting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
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
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel.CraftingDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


private val SECTION_HEADERS = listOf(
	SectionHeaderData(
		"Affected Crafting Station",
		"Needed for this station upgrade",
		Lucide.TrendingUp
	),
	SectionHeaderData(
		"Affected Food Station",
		"The food station improved by this item.",
		Lucide.TrendingUp
	),
	SectionHeaderData(
		"Upgrades",
		"Improve crafting stations by building structures nearby. Higher levels unlock new recipes and allow stronger item upgrades.",
		Lucide.TrendingUp
	),
	SectionHeaderData(
		"Requirements",
		"Components needed to build this station.",
		Lucide.ScrollText
	),
)
private val ICONS = listOf(
	Lucide.Utensils,
	Lucide.FlaskRound,
	Lucide.Swords,
	Lucide.Shield,
	Lucide.Cuboid,
	Lucide.OctagonAlert,
	Lucide.House,
	Lucide.Gavel
)

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
		uiState = { uiState },
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
	val scrollState = rememberScrollState()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val upgHeader =
		when (uiState.craftingObject?.subCategory) {
			CraftingSubCategory.CRAFTING_UPGRADER.toString() -> SECTION_HEADERS[0]
			CraftingSubCategory.CRAFTING_UPGRADER_FOOD.toString() -> SECTION_HEADERS[1]
			else -> SECTION_HEADERS[2]
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
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(scrollState),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,
				) {
					FramedImage(
						craftingObject.imageUrl,
						250.dp,
						contentScale = ContentScale.Fit
					)

					Text(
						craftingObject.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)

					SlavicDivider()

					Box(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)) {
						DetailExpandableText(
							text = craftingObject.description,
						)
					}

					UiSection(
						state = uiState.craftingMaterialToBuild
					) { data ->
						SectionHeader(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp),
							data = SECTION_HEADERS[3]
						)
						NestedGrid(
							nestedItems = NestedItems(items = data),
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

					UiSection(
						state = uiState.craftingUpgraderObjects
					) { data ->
						SectionHeader(
							modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
							data = upgHeader
						)
						NestedGrid(
							nestedItems = NestedItems(items = data),
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

					DroppedItemSection(
						state = uiState.craftingFoodProducts,
						handleClick = handleClick,
						icon = ICONS[0],
						title = "Food Items",
						subTitle = "Food items that can be created at this crafting station",
					)
					DroppedItemSection(
						state = uiState.craftingMeadProducts,
						handleClick = handleClick,
						icon = ICONS[1],
						title = "Mead Items",
						subTitle = "Mead items that can be created at this crafting station",
					)

					DroppedItemSection(
						state = uiState.craftingWeaponProducts,
						handleClick = handleClick,
						icon = ICONS[2],
						title = "Weapon Items",
						subTitle = "Weapon items that can be created at this crafting station",
					)

					DroppedItemSection(
						state = uiState.craftingArmorProducts,
						handleClick = handleClick,
						icon = ICONS[3],
						title = "Armor Items",
						subTitle = "Armor items that can be created at this crafting station",
					)


					DroppedItemSection(
						state = uiState.craftingMaterialProducts,
						handleClick = handleClick,
						icon = ICONS[4],
						title = "Material Items",
						subTitle = "Materials that can be created at this crafting station",
					)
					DroppedItemSection(
						state = uiState.craftingMaterialRequired,
						handleClick = handleClick,
						icon = ICONS[5],
						title = "Fuel Items",
						subTitle = "Items required as fuel for this station",
					)
					DroppedItemSection(
						state = uiState.craftingBuildingMaterialProducts,
						handleClick = handleClick,
						icon = ICONS[6],
						title = "Building Materials",
						subTitle = "Building materials that can be created at this crafting station",
					)
					DroppedItemSection(
						state = uiState.craftingToolProducts,
						handleClick = handleClick,
						icon = ICONS[7],
						title = "Tool Items",
						subTitle = "Tools that can be created at this crafting station",
					)

					Spacer(
						modifier = Modifier
							.fillMaxWidth()
							.height(70.dp)
					)
				}
			}
			AnimatedBackButton(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp),
				scrollState = scrollState,
				onBack = onBack
			)
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


@Composable
private fun DroppedItemSection(
	state: UIState<List<CraftingProducts>>,
	handleClick: (ItemData) -> Unit,
	icon: ImageVector,
	starLevel: Int = 0,
	title: String,
	subTitle: String
) {
	UiSection(
		state = state,
	) { data ->
		DroppedItemsSection(
			onItemClick = handleClick,
			list = data,
			icon = { icon },
			starLevel = starLevel,
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
