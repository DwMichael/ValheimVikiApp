package com.rabbitv.valheimviki.presentation.detail.material.valuable


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
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.User
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.material.valuable.model.ValuableMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.valuable.model.ValuableMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.valuable.viewmodel.ValuableMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun ValuableMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: ValuableMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(ValuableMaterialUiEvent.ToggleFavorite)
	}
	ValuableMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)
}

@Composable
fun ValuableMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: ValuableMaterialUiState,
) {

    val scrollState = rememberScrollState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this item",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)

	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "Creatures that drop this material",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)

	val npcData = HorizontalPagerData(
		title = "NPC",
		subTitle = "NPC from whom you can buy this item",
		icon = Lucide.User,
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
						state = uiState.creatures
					) { creatures ->

						HorizontalPagerSection(
							list = creatures,
							data = creatureData,
							onItemClick = handleItemClick,
						)
					}

					UiSection(
						state = uiState.npc
					) { npc ->
						HorizontalPagerSection(
							list = npc,
							data = npcData,
							onItemClick = handleItemClick,
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


@Preview("ValuableMaterialDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {
	ValheimVikiAppTheme {
		ValuableMaterialDetailContent(
			uiState = ValuableMaterialUiState(),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { }
		)
	}
}
