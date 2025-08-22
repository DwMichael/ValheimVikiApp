package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop


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
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBoss
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model.MiniBossDropUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model.MiniBossDropUiState
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.viewmodel.MiniBossDropDetailViewModel
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun MiniBossDropDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,

	viewModel: MiniBossDropDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(MiniBossDropUiEvent.ToggleFavorite)
	}

	MiniBossDropDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}



@Composable
fun MiniBossDropDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: MiniBossDropUiState,
) {

	val scrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }

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
						SlavicDivider()
					}

					when (val miniBossState = uiState.miniBoss) {
						is UIState.Success -> {
							miniBossState.data?.let { miniBoss ->
								ImageWithTopLabel(
									onItemClick = { clickedItemId ->
										val destination = NavigationHelper.routeToCreature(
											miniBoss.subCategory,
											miniBoss.id
										)
										onItemClick(destination)
									},
									itemData = miniBoss,
									subTitle = "Boss from witch this item drop",
									contentScale = ContentScale.Crop,
								)
								SlavicDivider()
							}
						}
						else -> {}
					}

					when (val npcState = uiState.npc) {
						is UIState.Success -> {
							npcState.data?.let { npc ->
								ImageWithTopLabel(
									onItemClick = { clickedItemId ->
										val destination = NavigationHelper.routeToCreature(
											npc.subCategory,
											npc.id
										)
										onItemClick(destination)
									},
									itemData = npc,
									subTitle = "Npc that give you quest for this item",
									contentScale = ContentScale.Crop,
								)
							}
						}
						else -> {}
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
		MiniBossDropDetailContent(
			uiState = MiniBossDropUiState(
				material = FakeData.generateFakeMaterials()[0],
				miniBoss =  UIState.Success( FakeData.generateFakeCreatures()[0].toMiniBoss()),
			),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { }
		)
	}

}
