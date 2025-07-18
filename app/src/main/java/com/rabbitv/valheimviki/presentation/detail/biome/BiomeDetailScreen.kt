package com.rabbitv.valheimviki.presentation.detail.biome

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
import com.rabbitv.valheimviki.presentation.detail.biome.viewmodel.BiomeDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val biomeUiState by viewModel.biomeUiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}

	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")

	BiomeDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		sharedTransitionScope = sharedTransitionScope,
		animatedVisibilityScope = animatedVisibilityScope,
		biomeUiState = biomeUiState,
		onToggleFavorite = onToggleFavorite
	)

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	biomeUiState: BiomeDetailUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val creatureData = HorizontalPagerData(
		title = "Creatures",
		subTitle = "Creatures you may encounter in this biome",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)
	val oreDepositData = HorizontalPagerData(
		title = "Ore Deposits",
		subTitle = "Ore Deposits you may encounter in this biome",
		icon = Lucide.Pickaxe,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)

	val materialData = HorizontalPagerData(
		title = "Materials",
		subTitle = "Unique materials you may encounter in this biome",
		icon = Lucide.Gem,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)

	val pointOfInterestData = HorizontalPagerData(
		title = "Points Of Interest",
		subTitle = "Points Of Interest you may encounter in this biome",
		icon = Lucide.House,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)
	val treeData = HorizontalPagerData(
		title = "Trees",
		subTitle = "Trees you may encounter in this biome",
		icon = Lucide.Trees,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)

	Scaffold(
		content = { padding ->
			AnimatedContent(
				targetState = biomeUiState.isLoading,
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
				} else if (biomeUiState.biome != null) {
					BgImage()
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(padding)
					) {
						Column(
							modifier = Modifier
								.testTag("BiomeDetailScreen")
								.fillMaxSize()
								.verticalScroll(scrollState, enabled = !isRunning),
							verticalArrangement = Arrangement.Top,
							horizontalAlignment = Alignment.Start,
						) {
							MainDetailImageAnimated(
								sharedTransitionScope = sharedTransitionScope,
								animatedVisibilityScope = animatedVisibilityScope,
								id = biomeUiState.biome.id,
								imageUrl = biomeUiState.biome.imageUrl,
								title = biomeUiState.biome.name
							)

							DetailExpandableText(
								text = biomeUiState.biome.description,
								boxPadding = BODY_CONTENT_PADDING.dp
							)
							biomeUiState.mainBoss?.let { mainBoss ->
								ImageWithTopLabel(
									itemData = mainBoss,
									subTitle = "BOSS",
									onItemClick = { clickedItemId ->
										val destination = NavigationHelper.routeToCreature(
											biomeUiState.mainBoss.subCategory,
											biomeUiState.mainBoss.id
										)
										onItemClick(destination)
									},
								)
							}
							SlavicDivider()
							if (biomeUiState.relatedCreatures.isNotEmpty()) {
								HorizontalPagerSection(
									list = biomeUiState.relatedCreatures,
									data = creatureData,
									onItemClick = { clickedItemId ->
										val creature =
											biomeUiState.relatedCreatures.find { it.id == clickedItemId }
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

							if (biomeUiState.relatedOreDeposits.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = biomeUiState.relatedOreDeposits,
									data = oreDepositData,
									onItemClick = { clickedItemId ->
										val destination =
											WorldDetailDestination.OreDepositDetail(oreDepositId = clickedItemId)
										onItemClick(destination)
									},
								)
							}

							if (biomeUiState.relatedMaterials.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = biomeUiState.relatedMaterials,
									data = materialData,
									onItemClick = { clickedItemId ->
										val material =
											biomeUiState.relatedMaterials.find { it.id == clickedItemId }
										material?.let {
											val destination = NavigationHelper.routeToMaterial(
												it.subCategory,
												it.id
											)
											onItemClick(destination)
										}
									}
								)
							}

							if (biomeUiState.relatedPointOfInterest.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = biomeUiState.relatedPointOfInterest,
									data = pointOfInterestData,
									onItemClick = { clickedItemId ->
										val destination =
											WorldDetailDestination.PointOfInterestDetail(
												pointOfInterestId = clickedItemId
											)
										onItemClick(destination)
									}
								)
							}
							if (biomeUiState.relatedTrees.isNotEmpty()) {
								TridentsDividedRow()
								HorizontalPagerSection(
									list = biomeUiState.relatedTrees,
									data = treeData,
									onItemClick = { clickedItemId ->
										val destination =
											WorldDetailDestination.TreeDetail(treeId = clickedItemId)
										onItemClick(destination)
									}
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
								isFavorite = biomeUiState.isFavorite,
								onToggleFavorite ={
									onToggleFavorite(biomeUiState.biome.toFavorite(), biomeUiState.isFavorite)
								} ,
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
	)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("BiomeDetailContent", showBackground = true)
@Composable
fun PreviewBiomeDetailContent() {
	val fakeBiome = Biome(
		id = "1",
		imageUrl = "https://via.placeholder.com/600x320.png?text=Biome+Image",
		name = "Przykładowy Biome",
		description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
		category = "BIOME",
		order = 1
	)
	val fakeMainBoss = MainBoss(
		id = "boss1",
		imageUrl = "https://via.placeholder.com/400x200.png?text=MainBoss+Image",
		category = "CREATURE",
		subCategory = "BOSS",
		name = "Przykładowy MainBoss",
		description = "Przykładowy opis głównego bossa.",
		order = 1,
		baseHP = 1500,
		weakness = "Ogień",
		resistance = "Lód",
		baseDamage = "100",
		collapseImmune = "False",
		forsakenPower = "High"
	)
	val creatureList = FakeData.generateFakeCreatures()
	val oreDeposit = FakeData.generateFakeOreDeposits()
	val materials = FakeData.generateFakeMaterials()
	val uiState = BiomeDetailUiState(
		biome = fakeBiome,
		mainBoss = fakeMainBoss,
		relatedCreatures = creatureList,
		relatedOreDeposits = oreDeposit,
		relatedMaterials = materials,
		relatedPointOfInterest = emptyList(),
		relatedTrees = emptyList()
	)

	ValheimVikiAppTheme {
		SharedTransitionLayout {
			AnimatedVisibility(visible = true) {
				BiomeDetailContent(
					onBack = { },
					sharedTransitionScope = this@SharedTransitionLayout,
					animatedVisibilityScope = this,
					biomeUiState = uiState,
					onItemClick = { _ -> {} },
					onToggleFavorite = {_,_ -> {}},
				)
			}
		}
	}
}

