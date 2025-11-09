package com.rabbitv.valheimviki.presentation.detail.material.offerings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.Sword
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiState
import com.rabbitv.valheimviki.presentation.detail.material.offerings.viewmodel.OfferingsDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@Composable
fun OfferingsDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: OfferingsDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(OfferingUiEvent.ToggleFavorite)
	}
	OfferingsDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@Composable
fun OfferingsDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: OfferingUiState,
) {

    val scrollState = rememberScrollState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	val aggressiveCreatureData = HorizontalPagerData(
		title = "Aggressive Creatures",
		subTitle = "Aggressive creatures that drop this material",
		icon = Lucide.Sword,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)

	val passiveCreatureData = HorizontalPagerData(
		title = "Passive Creatures",
		subTitle = "Passive creatures that drop this material",
		icon = Lucide.Rabbit,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)

	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this item",
		icon = Lucide.MapPinned,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)

	val altarsData = HorizontalPagerData(
		title = "Altars",
		subTitle = "Altars where you can offer this item",
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
                        )

                    }

					UiSection(
						state = uiState.aggressive
					) { aggressive ->
						HorizontalPagerSection(
							list = aggressive,
							data = aggressiveCreatureData,
							onItemClick = handleItemClick,
						)
					}

					UiSection(
						state = uiState.passive
					) { passive ->
						HorizontalPagerSection(
							list = passive,
							data = passiveCreatureData,
							onItemClick = handleItemClick,
						)
					}

					UiSection(
						state = uiState.pointsOfInterest
					) { pointsOfInterest ->
						HorizontalPagerSection(
							list = pointsOfInterest,
							data = pointsOfInterestData,
							onItemClick = handleItemClick
						)
					}

					UiSection(
						state = uiState.altars
					) { altars ->

						HorizontalPagerSection(
							list = altars,
							data = altarsData,
							onItemClick = handleItemClick
						)
					}

					UiSection(
						state = uiState.craftingStation
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
			uiState.material?.let {
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

	val agg = listOf(
		Creature(
			id = "creature002",
			category = "Aggressive",
			subCategory = "Undead",
			imageUrl = "https://example.com/frost_draugr.png",
			name = "Frost Draugr",
			description = "An ancient warrior risen from the dead in the frozen mountains. Carries ice-encrusted weapons and armor.",
			order = 2,
			levels = 2,
			baseHP = 150,
			weakness = "Fire, Spirit",
			resistance = "Frost, Poison",
			baseDamage = "35-45",
		)
	)
	ValheimVikiAppTheme {
		OfferingsDetailContent(
			uiState = OfferingUiState(
				material = FakeData.generateFakeMaterials()[0],
				aggressive = UIState.Success(agg.toAggressiveCreatures()),
				passive = UIState.Success(agg.toPassiveCreatures()),
				pointsOfInterest = UIState.Success(FakeData.pointOfInterest),
				altars = UIState.Success(FakeData.pointOfInterest),
				craftingStation = UIState.Success(FakeData.fakeCraftingObjectList())
			),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = {}
		)
	}

}
