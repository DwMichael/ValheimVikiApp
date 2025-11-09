package com.rabbitv.valheimviki.presentation.detail.mead

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Clock2
import com.composables.icons.lucide.ClockArrowDown
import com.composables.icons.lucide.CookingPot
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Layers2
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
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
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedGrid
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedItems
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiState
import com.rabbitv.valheimviki.presentation.detail.mead.viewmodel.MeadDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.shouldShowValue

@Composable
fun MeadDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	category: MeadSubCategory,
	viewModel: MeadDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(MeadDetailUiEvent.ToggleFavorite)
	}
	MeadDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
		category = category
	)

}


@Composable
fun MeadDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: MeadDetailUiState,

	category: MeadSubCategory
) {

	val scrollState = rememberScrollState()
	val craftingStationPainter = painterResource(R.drawable.food_bg)
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}


	val mead = uiState.mead
	val showStatsSection =
		mead != null && (shouldShowValue(mead.duration) || shouldShowValue(mead.cooldown) || shouldShowValue(
			mead.recipeOutput
		))



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
			uiState.mead?.let { mead ->
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
					FramedImage(mead.imageUrl)
					Text(
						mead.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					if (mead.description != null) {
						Box(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)) {
							DetailExpandableText(
								text = mead.description,
							)
						}
					}
					UiSection(
						state = uiState.recipeItems
					) { data ->
						if (data.isNotEmpty()) {
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.wrapContentHeight()
									.padding(horizontal = BODY_CONTENT_PADDING.dp),
								contentAlignment = Alignment.Center
							) {
								SectionHeader(
									modifier = Modifier.fillMaxWidth(),
									data = SectionHeaderData(
										title = "Recipe",
										subTitle = "Ingredients required to craft this item",
										icon = if (category == MeadSubCategory.MEAD_BASE) Lucide.CookingPot else Lucide.FlaskRound,
									)
								)
							}

							Spacer(modifier = Modifier.padding(6.dp))
							NestedGrid(
								nestedItems = NestedItems(items = data),
								horizontalPadding = BODY_CONTENT_PADDING.dp,
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
					}
					if (showStatsSection) {
						TridentsDividedRow("Stats")
						Column(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = BODY_CONTENT_PADDING.dp),
							verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
						) {
							if (shouldShowValue(mead.duration)) {
								AnimatedStatCard(
									id = "duration_stat",
									icon = Lucide.Clock2,
									label = "Duration",
									value = "${mead.duration.toString()} min",
									details = "How long this potion's effects remain active after consumption. The timer begins immediately upon eating and cannot be paused or extended.",
								)
							}
							if (shouldShowValue(mead.cooldown)) {
								AnimatedStatCard(
									id = "cooldown_stat",
									icon = Lucide.ClockArrowDown,
									label = "Cooldown",
									value = mead.cooldown.toString(),
									details = "The cooldown is the time you must wait before consuming another potion or mead of the same type. It prevents immediate re-use and encourages strategic planning in combat or exploration.",
								)
							}
							if (shouldShowValue(mead.recipeOutput)) {
								AnimatedStatCard(
									id = "stack_size_stat",
									icon = Lucide.Layers2,
									label = "Stack size",
									value = mead.recipeOutput.toString(),
									details = "The amount of meads produced by fermenting the mead base for two in-game days.",
								)
							}
						}
					}
					when (val state = uiState.craftingCookingStation) {
						is UIState.Error -> Unit
						is UIState.Loading -> {
							SlavicDivider()
							LoadingIndicator(paddingValues = PaddingValues(16.dp))
						}

						is UIState.Success -> {
							SlavicDivider()
							state.data?.let { craftingObject ->
								CardImageWithTopLabel(
									onClickedItem = handleClick,
									itemData = craftingObject,
									subTitle = if (category == MeadSubCategory.MEAD_BASE) "Requires cooking station" else "Requires fermenting station",
									contentScale = ContentScale.Fit,
									painter = craftingStationPainter
								)
							}
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
			uiState.mead?.let {
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


@Preview("MeadDetailContentPreview", showBackground = true)
@Composable
fun PreviewMeadDetailContentCooked() {
	val exampleMead = Mead(
		id = "mead_tasty",
		imageUrl = "https://example.com/images/mead_tasty.png",
		category = "Consumable",
		subCategory = "Mead",
		name = "Tasty Mead",
		description = "Increases stamina regeneration but reduces health regeneration.",
		recipeOutput = "6",
		effect = "Stamina Regen +300%, Health Regen -50%",
		duration = "10:00",
		cooldown = "02:00",
		order = 1
	)

	val craftingStation = CraftingObject(
		id = "workbench",
		imageUrl = "https://example.com/images/workbench.png",
		category = "Crafting Station",
		subCategory = "Basic",
		name = "Workbench",
		description = "Used for crafting basic tools, weapons, and building materials.",
		order = 1
	)

	ValheimVikiAppTheme {
		MeadDetailContent(
			uiState = MeadDetailUiState(
				mead = exampleMead,
				craftingCookingStation = UIState.Success(craftingStation),
			),
			onBack = {},
			onItemClick = {},
			category = MeadSubCategory.MEAD_BASE,
			onToggleFavorite = {}
		)
	}
}