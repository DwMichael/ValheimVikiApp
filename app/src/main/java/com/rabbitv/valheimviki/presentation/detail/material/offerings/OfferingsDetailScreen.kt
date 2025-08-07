package com.rabbitv.valheimviki.presentation.detail.material.offerings


import android.os.Build
import androidx.annotation.RequiresApi
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
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Skull
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.creature.Creature
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
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiState
import com.rabbitv.valheimviki.presentation.detail.material.offerings.viewmodel.OfferingsDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun OfferingsDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: OfferingsDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	OfferingsDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun OfferingsDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: OfferingUiState,

	) {

	val scrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val aggressiveCreatureData = HorizontalPagerData(
		title = "Aggressive Creatures",
		subTitle = "Creatures from witch this material drop",
		icon = Lucide.Skull,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val passiveCreatureData = HorizontalPagerData(
		title = "Passive Creatures",
		subTitle = "Creatures from witch this material drop",
		icon = Lucide.PawPrint,
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
		title = "Altar",
		subTitle = "Altar where you can summon boss with this item",
		icon = Lucide.Flame,
		iconRotationDegrees = 0f,
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
					if (uiState.aggressive.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.aggressive,
							data = aggressiveCreatureData,
							onItemClick = handleItemClick,
						)
					}
					if (uiState.passive.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.passive,
							data = passiveCreatureData,
							onItemClick = handleItemClick,
						)
					}
					if (uiState.pointsOfInterest.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.pointsOfInterest,
							data = pointsOfInterestData,
							onItemClick = handleItemClick
						)
					}
					if (uiState.altars.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.altars,
							data = altarsData,
							onItemClick = handleItemClick
						)
					}
					if (uiState.craftingStation.isNotEmpty()) {
						TridentsDividedRow()
						uiState.craftingStation.forEach { craftingStation ->
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
				aggressive = agg.toAggressiveCreatures(),
				passive = FakeData.generateFakeCreatures().toPassiveCreatures(),
				isLoading = false,
				error = null
			),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { _, _ -> {} }
		)
	}

}
