package com.rabbitv.valheimviki.presentation.detail.material.metal


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.ScrollText
import com.composables.icons.lucide.TreePine
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.material.metal.model.MetalMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.metal.viewmodel.MetalMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MetalMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MetalMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	MetalMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MetalMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: MetalMaterialUiState,

	) {

	val scrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }


	val pointOfInterestData = HorizontalPagerData(
		title = "Point Of Interest",
		subTitle = "Places where you can find this item",
		icon = Lucide.MapPinned,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)
	val oreDepositData = HorizontalPagerData(
		title = "Ore Deposit",
		subTitle = "Ore from witch you can mine this resource",
		icon = Lucide.Pickaxe,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)
	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "Creatures from witch this item drop",
		icon = Lucide.Rabbit,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
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
					uiState.biomes.forEach { biome ->
						TridentsDividedRow()
						CardWithOverlayLabel(
							onClickedItem = {
								val destination =
									WorldDetailDestination.BiomeDetail(biomeId = biome.id)
								onItemClick(destination)
							},
							painter = rememberAsyncImagePainter(biome.imageUrl),
							content = {
								Row {
									Box(
										modifier = Modifier.fillMaxHeight()
									) {
										OverlayLabel(
											icon = Lucide.TreePine,
											label = " PRIMARY SPAWN",
										)
									}
									Text(
										biome.name.uppercase(),
										style = MaterialTheme.typography.bodyLarge,
										modifier = Modifier
											.align(Alignment.CenterVertically)
											.fillMaxWidth()
											.padding(8.dp),
										color = Color.White,
										textAlign = TextAlign.Center
									)
								}

							}
						)

					}
					if (uiState.pointOfInterests.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.pointOfInterests,
							data = pointOfInterestData,
							onItemClick = { clickedItemId ->
								val destination =
									WorldDetailDestination.PointOfInterestDetail(pointOfInterestId = clickedItemId)
								onItemClick(destination)
							}
						)
					}
					if (uiState.oreDeposits.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.oreDeposits,
							data = oreDepositData,
							onItemClick = { clickedItemId ->
								val destination =
									WorldDetailDestination.OreDepositDetail(oreDepositId = clickedItemId)
								onItemClick(destination)
							},
						)
					}
					if (uiState.creatures.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.creatures,
							data = creatureData,
							onItemClick = { clickedItemId ->
								val creature = uiState.creatures.find { it.id == clickedItemId }
								creature?.let {
									val destination = NavigationHelper.routeToCreature(
										it.subCategory.toString(),
										it.id
									)
									onItemClick(destination)
								}
							},
						)
					}
					if (uiState.requiredMaterials.isNotEmpty()) {
						TridentsDividedRow()
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp)
						) {
							SectionHeader(
								data = HorizontalPagerWithHeaderData(
									title = "Required Items",
									subTitle = "Items needed to build this material.",
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
							for (item in uiState.requiredMaterials) {
								CustomItemCard(
									onItemClick = {
										val destination =
											NavigationHelper.routeToMaterial(
												item.itemDrop.subCategory,
												item.itemDrop.id
											)
										onItemClick(destination)

									},
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = item.itemDrop.imageUrl,
									name = item.itemDrop.name,
									quantity = item.quantityList.firstOrNull()
								)
							}
						}
					}
					if (uiState.craftingStations.isNotEmpty()) {
						TridentsDividedRow()
						uiState.craftingStations.forEach { craftingStation ->
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
						onToggleFavorite(material.toFavorite(), uiState.isFavorite)
					}
				)
			}
		}
	}
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview("ToolDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {


	ValheimVikiAppTheme {
		MetalMaterialDetailContent(
			uiState = MetalMaterialUiState(
				material = FakeData.generateFakeMaterials()[0],
				isLoading = false,
				error = null
			),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { _, _ -> {} }
		)
	}

}
