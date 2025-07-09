package com.rabbitv.valheimviki.presentation.detail.ore_deposit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.composables.icons.lucide.Combine
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pickaxe
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.EquipmentDetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiState
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.viewmodel.OreDepositViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OreDepositDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: OreDepositViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope,
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	OreDepositDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
		sharedTransitionScope = sharedTransitionScope,
		animatedVisibilityScope = animatedVisibilityScope
	)
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OreDepositDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: OreDepositUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val pickaxesData = HorizontalPagerData(
		title = "Pickaxes",
		subTitle = "List of pickaxes that can mine this ore out",
		icon = Lucide.Pickaxe,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val craftingObjectData = HorizontalPagerData(
		title = "Extractor",
		subTitle = "List of extractors that can extract resource",
		icon = Lucide.Combine,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	Scaffold { padding ->
		AnimatedContent(
			targetState = uiState.isLoading,
			modifier = Modifier.fillMaxSize(),
			transitionSpec = {
				if (!targetState && initialState) {
					fadeIn(
						animationSpec = tween(
							durationMillis = 650,
							delayMillis = 0
						)
					) + slideInVertically(
						initialOffsetY = { height -> height / 25 },
						animationSpec = tween(
							durationMillis = 650,
							delayMillis = 0,
							easing = EaseOutCubic
						)
					) togetherWith
							fadeOut(
								animationSpec = tween(
									durationMillis = 200
								)
							)
				} else {
					fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
							fadeOut(animationSpec = tween(durationMillis = 300))
				}
			},
			label = "LoadingStateTransition"
		) { isLoading ->
			if (isLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding),
					contentAlignment = Alignment.Center
				) {
					Box(modifier = Modifier.size(45.dp))
				}
			} else if (uiState.oreDeposit != null) {
				BgImage()
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(padding)
				) {
					Column(
						modifier = Modifier
							.testTag("OreDepositDetailScreen")
							.fillMaxSize()
							.verticalScroll(scrollState, enabled = !isRunning),
						verticalArrangement = Arrangement.Top,
						horizontalAlignment = Alignment.Start,
					) {
						MainDetailImageAnimated(
							sharedTransitionScope = sharedTransitionScope,
							animatedVisibilityScope = animatedVisibilityScope,
							id = uiState.oreDeposit.id,
							imageUrl = uiState.oreDeposit.imageUrl,
							title = uiState.oreDeposit.name
						)

						DetailExpandableText(
							text = uiState.oreDeposit.description.toString(),
							boxPadding = BODY_CONTENT_PADDING.dp
						)
						if (uiState.relatedBiomes.isNotEmpty()) {
							SlavicDivider()
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
									onClickedItem = {
										val destination =
											WorldDetailDestination.BiomeDetail(
												biomeId = biome.id
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

						if (uiState.relatedTools.isNotEmpty()) {
							TridentsDividedRow()
							HorizontalPagerSection(
								list = uiState.relatedTools,
								data = pickaxesData,
								onItemClick = { clickedItemId ->
									val destination =
										EquipmentDetailDestination.ToolDetail(clickedItemId)
									onItemClick(destination)
								},
							)
						}
						if (uiState.craftingStation.isNotEmpty()) {
							TridentsDividedRow()
							HorizontalPagerSection(
								list = uiState.craftingStation,
								data = craftingObjectData,
								onItemClick = { clickedItemId ->
									val destination =
										BuildingDetailDestination.CraftingObjectDetail(
											clickedItemId
										)
									onItemClick(destination)
								},
							)
						}
						if (uiState.relatedMaterials.isNotEmpty()) {
							TridentsDividedRow()
							DroppedItemsSection(
								onItemClick = { clickedItemId, subCategory ->
									val destination =
										NavigationHelper.routeToMaterial(subCategory, clickedItemId)
									onItemClick(destination)
								},
								list = uiState.relatedMaterials,
								icon = Lucide.Gem,
								starLevel = 0,
								title = "Materials",
								subTitle = "Unique drops are obtained by mining this ore",
							)
						}
						Box(modifier = Modifier.size(45.dp))
					}
					if (!isRunning) {
						AnimatedBackButton(
							modifier = Modifier
								.align(Alignment.TopStart)
								.padding(16.dp),
							scrollState = scrollState,
							onBack = onBack,
						)
						FavoriteButton(
							modifier = Modifier
								.align(Alignment.TopEnd)
								.padding(16.dp),
							isFavorite = uiState.isFavorite,
							onToggleFavorite = {
								onToggleFavorite(
									uiState.oreDeposit.toFavorite(),
									uiState.isFavorite
								)
							}
						)

					}
				}
			} else {
				Box(
					Modifier
						.fillMaxSize()
						.padding(padding)
				)
			}
		}
	}
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("OreDepositDetailContent", showBackground = true)
@Composable
fun PreviewOreDepositDetailContent() {
	ValheimVikiAppTheme {
		SharedTransitionLayout {
			AnimatedVisibility(visible = true) {
				val testDescription = """
         <p><b>Glimmerwood Saplings</b> are rare, bioluminescent flora found deep within the ancient Whispering Woods. When carefully harvested, they yield Starlight Essence and Petrified Bark, key components for crafting high-level illusion potions and light-enchanted gear.</p>
<br>
<p>These saplings are only visible during the darkest hours of the night, emitting a soft, pulsating glow. The rhythm of the light indicates the plant's maturity; a slow, steady pulse means it's ready for harvest. Attempting to cut it with standard tools will cause it to shatter into useless dust. Only specially crafted <b>Moon-silver Shears</b> can harvest it safely.</p>
<br>
<p>Alternatively, a skilled botanist can use a <b>Verdant Pouch</b> to carefully uproot a young sapling. If transplanted into an area of complete darkness and magical soil, it can be cultivated. While a cultivated Glimmerwood produces less Starlight Essence, it provides a slow but steady source for the patient alchemist.</p>
<br>
<h3>Lore & Cultivation</h3>
<p>Ancient legends state that Glimmerwood Saplings grow only where a shard of a falling star has touched the earth. Their cultivation is a delicate process governed by strict celestial and environmental rules:</p>
<ul>
    <li>The cultivation area must be shielded from all external light sources.</li>
    <li>It must be planted in soil infused with at least one magical essence.</li>
    <li>The sapling only grows when the "Seeker's Constellation" is visible in the night sky.</li>
    <li>Watering must be done with Purified Water or at a Moon Well.</li>
</ul>
<p>Failure to meet these conditions will cause the cultivated sapling to wither and lose its magical properties within a single night.</p>
        """.trimIndent()

				// Create mock data
				val mockOreDeposit = OreDeposit(
					id = "2",
					name = "Silver Vein",
					imageUrl = "https://example.com/silver-vein.jpg",
					description = testDescription,
					category = "",
					order = 1,
				)

				val mockUiState = OreDepositUiState(
					isLoading = false,
					oreDeposit = mockOreDeposit,
					relatedBiomes = emptyList(),
					relatedTools = emptyList(),
					craftingStation = emptyList(),
					relatedMaterials = emptyList()
				)

				OreDepositDetailContent(
					onBack = {},
					onItemClick = {},
					onToggleFavorite = { _, _ -> {} },
					uiState = mockUiState,
					sharedTransitionScope = this@SharedTransitionLayout,
					animatedVisibilityScope = this,
				)
			}
		}
	}
}


