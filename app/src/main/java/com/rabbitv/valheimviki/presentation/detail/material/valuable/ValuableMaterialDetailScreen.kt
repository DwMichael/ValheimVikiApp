package com.rabbitv.valheimviki.presentation.detail.material.valuable


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.ShoppingBag
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.material.valuable.model.ValuableMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.valuable.viewmodel.ValuableMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ValuableMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: ValuableMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	ValuableMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ValuableMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: ValuableMaterialUiState,
) {

	val scrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }

	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "From those creatures this item drop",
		icon = Lucide.Rabbit,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val npcData = HorizontalPagerData(
		title = "Npc",
		subTitle = "Those Npcs buy this item from you",
		icon = Lucide.ShoppingBag,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this seed",
		icon = Lucide.MapPinned,
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
					if (uiState.pointsOfInterest.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.pointsOfInterest,
							data = pointsOfInterestData,
							onItemClick = { clickedItemId ->
								val destination =
									WorldDetailDestination.PointOfInterestDetail(pointOfInterestId = clickedItemId)
								onItemClick(destination)
							}
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
					if (uiState.npc.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.npc,
							data = npcData,
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
@Preview("ValuableMaterialDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {


	ValheimVikiAppTheme {
		ValuableMaterialDetailContent(
			uiState = ValuableMaterialUiState(),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { _, _ -> {} }
		)
	}

}
