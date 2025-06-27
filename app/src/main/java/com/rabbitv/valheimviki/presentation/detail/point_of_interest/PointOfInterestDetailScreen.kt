package com.rabbitv.valheimviki.presentation.detail.point_of_interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Goal
import com.composables.icons.lucide.HandCoins
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiState
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.viewmodel.PointOfInterestViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun PointOfInterestDetailScreen(
	onBack: () -> Unit,
	viewModel: PointOfInterestViewModel = hiltViewModel()
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()


	PointOfInterestDetailContent(
		onBack = onBack,
		uiState = uiState
	)

}


@Composable
fun PointOfInterestDetailContent(
	onBack: () -> Unit,
	uiState: PointOfInterestUiState,
) {
	val mainPainter = painterResource(R.drawable.main_background)

	val altarOfferings = HorizontalPagerData(
		title = "Offerings",
		subTitle = "List of offerings that are needed to summon boss",
		icon = Lucide.HandCoins,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val itemsToFind = HorizontalPagerData(
		title = "Items",
		subTitle = "Items that can be found in this place",
		icon = Lucide.Goal,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	Scaffold { innerPadding ->
		Image(
			painter = mainPainter,
			contentDescription = "BackgroundImage",
			contentScale = ContentScale.Crop,
			modifier = Modifier.fillMaxSize(),
		)
		Column(
			modifier = Modifier
				.testTag("PointOfInterestDetailScreen")
				.fillMaxSize()
				.padding(innerPadding),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.Start,
		) {
			uiState.pointOfInterest?.let { pOfIn ->
				MainDetailImage(
					onBack = onBack,
					imageUrl = pOfIn.imageUrl,
					name = pOfIn.name,
					textAlign = TextAlign.Center,
				)
				SlavicDivider()
				DetailExpandableText(text = pOfIn.description, boxPadding = BODY_CONTENT_PADDING.dp)
				if (uiState.relatedBiomes.isNotEmpty()) {
					TridentsDividedRow()
					Text(
						modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
						text = "PRIMARY SPAWNS",
						textAlign = TextAlign.Left,
						style = MaterialTheme.typography.titleSmall,
						maxLines = 1,
						overflow = TextOverflow.Visible
					)
					uiState.relatedBiomes.forEach { biome ->
						CardWithOverlayLabel(
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

				if (uiState.relatedOfferings.isNotEmpty()) {
					TridentsDividedRow()
					HorizontalPagerSection(
						list = uiState.relatedOfferings,
						data = altarOfferings,
					)
				}

				if (uiState.relatedMaterialDrops.isNotEmpty()) {
					TridentsDividedRow()
					DroppedItemsSection(
						list = uiState.relatedMaterialDrops,
						icon = Lucide.Gem,
						starLevel = 0,
						title = "Materials",
						subTitle = "Unique drops are obtained in this place"
					)
				}
				Box(modifier = Modifier.size(45.dp))
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

	val uiState: PointOfInterestUiState = PointOfInterestUiState(
		pointOfInterest = FakeData.pointOfInterest[0],
		relatedBiomes = listOf(fakeBiome),
		relatedCreatures = FakeData.generateFakeCreatures(),
		relatedOfferings = FakeData.generateFakeMaterials(),
		isLoading = false,
		error = null
	)

	ValheimVikiAppTheme {
		PointOfInterestDetailContent(
			onBack = {},
			uiState = uiState
		)
	}
}