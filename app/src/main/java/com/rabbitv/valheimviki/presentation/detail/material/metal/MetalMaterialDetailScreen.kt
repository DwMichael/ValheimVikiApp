package com.rabbitv.valheimviki.presentation.detail.material.metal


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.ScrollText
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedGrid
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedItems
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.material.metal.model.MetalMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.metal.model.MetalMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.metal.viewmodel.MetalMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun MetalMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MetalMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(MetalMaterialUiEvent.ToggleFavorite)
	}
	MetalMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@Composable
fun MetalMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: MetalMaterialUiState,
) {
	val scrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }
	val handleClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	val biomesData = HorizontalPagerData(
		title = "Biomes",
		subTitle = "Biomes where you can find this material",
		icon = Lucide.Trees,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)

	val creaturesData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "Creatures that drop this material",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)

	val pointOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this item",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)

	val oreDepositsData = HorizontalPagerData(
		title = "Ore Deposits",
		subTitle = "Ore deposits where you can find this material",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)

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
			uiState.material?.let { material ->
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(scrollState)
						.padding(
							top = 20.dp,
							bottom = 70.dp
						),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,

					) {
					FramedImage(material.imageUrl)
					Text(
						material.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					material.description?.let {
						DetailExpandableText(
							text = material.description,
							boxPadding = BODY_CONTENT_PADDING.dp,
							isExpanded = isExpandable
						)

					}

					UiSection(
						state = uiState.biomes,
						divider = { SlavicDivider() }
					) { biomes ->
						HorizontalPagerSection(
							list = biomes,
							data = biomesData,
							onItemClick = handleClick,
						)
					}

					UiSection(
						state = uiState.creatures
					) { creatures ->
						HorizontalPagerSection(
							list = creatures,
							data = creaturesData,
							onItemClick = handleClick,
						)
					}

					UiSection(
						state = uiState.pointOfInterests
					) { pointOfInterests ->
						HorizontalPagerSection(
							list = pointOfInterests,
							data = pointOfInterestData,
							onItemClick = handleClick,
						)
					}

					UiSection(
						state = uiState.oreDeposits
					) { oreDeposits ->
						HorizontalPagerSection(
							list = oreDeposits,
							data = oreDepositsData,
							onItemClick = handleClick,
						)
					}

					UiSection(
						state = uiState.requiredMaterials
					) { requiredMaterials ->
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp)
						) {
							SectionHeader(
								data = SectionHeaderData(
									title = "Required Items",
									subTitle = "Items needed to build this material.",
									icon = Lucide.ScrollText,
								),

								)
						}

						Spacer(modifier = Modifier.padding(6.dp))
						NestedGrid(
							nestedItems = NestedItems(items = requiredMaterials),
							gridCells = 2
						) { item ->
							CustomItemCard(
								itemData = item.itemDrop,
								onItemClick = handleClick,
								fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
								imageUrl = item.itemDrop.imageUrl,
								name = item.itemDrop.name,
								quantity = item.quantityList.firstOrNull()
							)
						}
					}

					UiSection(
						state = uiState.craftingStations
					) { craftingStations ->
						craftingStations.forEach { craftingStation ->
							CardImageWithTopLabel(
								onClickedItem = {
									val destination =
										BuildingDetailDestination.CraftingObjectDetail(
											craftingObjectId = craftingStation.id
										)
									onItemClick(destination)
								},
								itemData = craftingStation,
								subTitle = "Crafting station where you can produce this item ",
								contentScale = ContentScale.Fit,
							)
							Spacer(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp))
						}
					}

				}
			}
			AnimatedBackButton(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp),
				scrollState = scrollState,
				onBack = onBack
			)
			uiState.material?.let { material ->
				FavoriteButton(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(16.dp),
					isFavorite = uiState.isFavorite,
					onToggleFavorite = {
						onToggleFavorite()
					}
				)
			}
		}
	}
}


@Preview("ToolDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {


	ValheimVikiAppTheme {
		MetalMaterialDetailContent(
			uiState = MetalMaterialUiState(
				material = FakeData.generateFakeMaterials()[0],

				),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { }
		)
	}

}
