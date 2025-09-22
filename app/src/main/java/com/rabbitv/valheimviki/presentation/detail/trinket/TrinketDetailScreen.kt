package com.rabbitv.valheimviki.presentation.detail.trinket


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Clock2
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.animated_stat_card.AnimatedStatCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.trinket.model.TrinketDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.trinket.model.TrinketDetailUiState
import com.rabbitv.valheimviki.presentation.detail.trinket.viewmodel.TrinketDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen20Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.ui.theme.YellowDTNotSelected
import com.rabbitv.valheimviki.utils.shouldShowValue


@Composable
fun TrinketDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: TrinketDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.uiEvent(
			TrinketDetailUiEvent.ToggleFavorite(
				favorite = favorite
			)
		)
	}
	TrinketDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState
	)
}

@Composable
fun TrinketDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: TrinketDetailUiState
) {

	val scrollState = rememberScrollState()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	BgImage()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentColor = PrimaryWhite
	) { innerPadding ->
		uiState.trinket?.let { trinket ->
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
			) {
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
					FramedImage(trinket.imageUrl)
					Text(
						trinket.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					trinket.description?.let {
						DetailExpandableText(
							text = it,
							collapsedMaxLine = 3,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
					}
					TridentsDividedRow()
					if (shouldShowValue(trinket.duration)) {
						AnimatedStatCard(
							modifier = Modifier.padding(10.dp),
							id = "adrenaline_stat",
							icon = Lucide.Clock2,
							label = stringResource(R.string.adrenaline),
							value = trinket.adrenaline.toString(),
							details = "Adrenaline is a personal resource used by Trinkets. It is represented by an orange bar in the HUD.\n\nAdrenaline is generated by player actions when the player has a trinket equipped. Unequipping a trinket does not reset adrenaline, only its generation is stopped. The maximum amount of adrenaline is defined by the equipped trinket.\n\nWhen the maximum amount of adrenaline is reached, all the adrenaline is consumed and the trinket power is activated. Adrenaline can build up again without delay, even while the trinket power is active.\n",
						)
					}

					if (shouldShowValue(trinket.duration)) {
						AnimatedStatCard(
							modifier = Modifier.padding(10.dp),
							id = "duration_stat",
							icon = Lucide.Clock2,
							label = stringResource(R.string.duration),
							value = "${trinket.duration.toString()} min",
							details = "How long this food's effects remain active after consumption. The timer begins immediately upon eating and cannot be paused or extended.",
						)
					}
					TridentsDividedRow()
					trinket.effects.let { effectContent ->
						if (effectContent.isNotEmpty()) {
							InfoSection(
								title = "Additional Effect",
								content = effectContent
							)
						}
					}

					when (val craftingObject = uiState.craftingObject) {
						is UIState.Error -> {}
						is UIState.Loading -> {
							TridentsDividedRow()
							LoadingIndicator(
								paddingValues = PaddingValues(16.dp)
							)
						}

						is UIState.Success -> {
							uiState.craftingObject.data?.let { craftingStation ->
								TridentsDividedRow()
								CardImageWithTopLabel(
									onClickedItem = handleClick,
									itemData = craftingObject.data,
									subTitle = "Crafting Station Needed to Make This Item",
									contentScale = ContentScale.Fit,
								)
							}
						}
					}

					Spacer(modifier = Modifier.height(40.dp))

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
						onToggleFavorite(uiState.trinket.toFavorite(), uiState.isFavorite)
					},
				)
			}

		}
	}
}

@Composable
fun InfoSection(
	title: String,
	content: List<String>,
	modifier: Modifier = Modifier
) {
	Column(
		// Changed from LazyColumn to Column
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 8.dp)
			.border(
				width = 1.dp,
				color = YellowDTBorder,
				shape = RoundedCornerShape(8.dp)
			)
			.background(
				color = ForestGreen20Dark,
				shape = RoundedCornerShape(8.dp)
			)
			.padding(horizontal = 16.dp, vertical = 12.dp),
	) {
		Text(
			text = AnnotatedString.fromHtml(
				title,
			),
			color = PrimaryWhite,
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold
		)
		Spacer(modifier = Modifier.height(8.dp))
		content.forEach { item -> // Changed from items() to forEach
			Text(
				text = AnnotatedString.fromHtml(
					item,
				),
				color = YellowDTNotSelected,
				style = MaterialTheme.typography.bodyLarge,
				lineHeight = 22.sp
			)
		}
	}
}

@Preview(name = "PreviewTrinketDetailScreen", showBackground = true)
@Composable
private fun PreviewTrinketDetailScreen() {

	val testTrinket = Trinket(
		id = "test_helmet",
		imageUrl = "",
		category = "Headgear",
		subCategory = "Helmet",
		name = "Hardcoded Test Helmet",
		description = "This is a hardcoded description to test the preview.",

		effects = listOf("Hardcoded Effect: +10 Magic"),
		order = 1
	)

	ValheimVikiAppTheme {
		TrinketDetailContent(
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { _, _ -> {} },
			uiState = TrinketDetailUiState(
				trinket = testTrinket,
				materials = UIState.Loading,
				craftingObject = UIState.Loading
			)

		)
	}
}