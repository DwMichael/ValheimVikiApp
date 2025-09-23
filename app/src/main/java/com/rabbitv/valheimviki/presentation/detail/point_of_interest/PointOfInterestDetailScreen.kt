package com.rabbitv.valheimviki.presentation.detail.point_of_interest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.HandCoins
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.Swords
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiEvent
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiState
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.viewmodel.PointOfInterestViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun PointOfInterestDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: PointOfInterestViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(PointOfInterestUiEvent.ToggleFavorite)
	}
	PointOfInterestDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState
	)
}


@Composable
fun PointOfInterestDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: PointOfInterestUiState,
) {
	val scrollState = rememberScrollState()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val altarOfferings = HorizontalPagerData(
		title = "Offerings",
		subTitle = "List of offerings that are needed to summon boss",
		icon = Lucide.HandCoins,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val weaponsData = HorizontalPagerData(
		title = "Weapons",
		subTitle = "Weapons that can be found in this place",
		icon = Lucide.Swords,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "Creatures related to this place",
		icon = Lucide.Skull,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	Scaffold { innerPadding ->

		BgImage()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			Column(
				modifier = Modifier
					.testTag("TreeDetailScreen")
					.fillMaxSize()
					.verticalScroll(scrollState),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.Start,
			) {
				uiState.pointOfInterest?.let { pOfIn ->
					MainDetailImage(
						imageUrl = pOfIn.imageUrl,
						name = pOfIn.name,
						textAlign = TextAlign.Center,
					)
					SlavicDivider()
					DetailExpandableText(
						text = pOfIn.description,
						boxPadding = BODY_CONTENT_PADDING.dp
					)
					UiSection(
						uiState.relatedBiomes
					)
					{ data ->
						Text(
							modifier = Modifier.align(Alignment.CenterHorizontally),
							text = "PRIMARY SPAWN",
							textAlign = TextAlign.Center,
							style = MaterialTheme.typography.titleLarge,
							maxLines = 1,
							overflow = TextOverflow.Visible
						)
						data.forEach { biome ->
							CardWithOverlayLabel(
								onClickedItem = {
									val destination =
										WorldDetailDestination.BiomeDetail(
											biomeId = biome.id,
											imageUrl = biome.imageUrl,
											title = biome.name,
										)
									onItemClick(destination)

								},
								painter = rememberAsyncImagePainter(biome.imageUrl),
								content = {
									Box(
										modifier = Modifier
											.fillMaxSize()
											.wrapContentHeight(Alignment.CenterVertically)
											.wrapContentWidth(Alignment.CenterHorizontally)
									) {
										Text(
											biome.name.uppercase(),
											style = MaterialTheme.typography.bodyLarge,
											modifier = Modifier,
											color = Color.White,
											textAlign = TextAlign.Center
										)
									}
								}
							)
						}
					}

					UiSection(uiState.relatedOfferings) { data ->
						HorizontalPagerSection(
							list = data,
							data = altarOfferings,
							onItemClick = handleClick,
						)
					}

					UiSection(uiState.relatedMaterialDrops) { data ->
						DroppedItemsSection(
							modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
							onItemClick = handleClick,
							list = data,
							icon = { Lucide.Gem },
							starLevel = 0,
							title = "Materials",
							subTitle = "Unique drops are obtained in this place"
						)
					}
					UiSection(uiState.relatedWeapons) { data ->
						HorizontalPagerSection(
							list = data,
							data = weaponsData,
							onItemClick = handleClick,
						)
					}
					UiSection(uiState.relatedCreatures) { data ->
						HorizontalPagerSection(
							list = data,
							data = creatureData,
							onItemClick = handleClick
						)
					}

				}
				Spacer(modifier = Modifier.height(90.dp))
			}
			AnimatedBackButton(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp),
				scrollState = scrollState,
				onBack = onBack,
			)
			uiState.pointOfInterest?.let { pointOfInterest ->
				FavoriteButton(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(16.dp),
					isFavorite = uiState.isFavorite,
					onToggleFavorite = {
						onToggleFavorite()
					},
				)
			}
		}
	}
}


@Preview("PointOfInterestDetailScreenPreview")
@Composable
fun PreviewPointOfInterestDetailScreen() {
	val fakeBiome = Biome(
		id = "1",
		imageUrl = "https://via.placeholder.com/600x320.png?text=Biome+Image",
		name = "Przykładowy Biome",
		description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
		category = "BIOME",
		order = 1
	)

	val uiState = PointOfInterestUiState(
		pointOfInterest = FakeData.pointOfInterest[0],
		relatedBiomes = UIState.Success(listOf(fakeBiome)),
		relatedCreatures = UIState.Success(FakeData.generateFakeCreatures()),
		relatedOfferings = UIState.Success(FakeData.generateFakeMaterials()),
	)

	ValheimVikiAppTheme {
		PointOfInterestDetailContent(
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { },
			uiState = uiState
		)
	}
}