package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.TreePine
import com.composables.icons.lucide.Trophy
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
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
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.dividers.GreenTorchesDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithImageAndTitle
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithTrophy
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.column.StatColumn
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.CustomRowLayout
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.viewmodel.MainBossScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MainBossScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val mainBossUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")

	MainBossContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		animatedVisibilityScope = animatedVisibilityScope,
		sharedTransitionScope = sharedTransitionScope,
		mainBossUiState = mainBossUiState
	)

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
	mainBossUiState: MainBossDetailUiState,
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val dropData = HorizontalPagerData(
		title = "Drop Items",
		subTitle = "Items that drop from boss after defeating him",
		icon = Lucide.Trophy,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val painter = painterResource(R.drawable.summoning_bg)
	val isStatInfoExpanded = remember { mutableStateOf(false) }
	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isStatInfoExpanded2 = remember { mutableStateOf(false) }
	val isStatInfoExpanded3 = remember { mutableStateOf(false) }
	val isStatInfoExpanded4 = remember { mutableStateOf(false) }


	Scaffold(
		content = { padding ->
			AnimatedContent(
				targetState = mainBossUiState.isLoading,
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
				} else if (mainBossUiState.mainBoss != null) {

					BgImage()
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(padding)
					) {
						Column(
							modifier = Modifier
								.testTag("MainBossDetailScreen")
								.fillMaxSize()
								.verticalScroll(scrollState, enabled = !isRunning),
							verticalArrangement = Arrangement.Top,
							horizontalAlignment = Alignment.Start,
						) {
							MainDetailImageAnimated(
								sharedTransitionScope = sharedTransitionScope,
								animatedVisibilityScope = animatedVisibilityScope,
								textAlign = TextAlign.Center,
								id = mainBossUiState.mainBoss.id,
								imageUrl = mainBossUiState.mainBoss.imageUrl,
								title = mainBossUiState.mainBoss.name
							)
							DetailExpandableText(
								text = mainBossUiState.mainBoss.description,
								boxPadding = BODY_CONTENT_PADDING.dp
							)
							TridentsDividedRow(text = "BOSS DETAIL")
							mainBossUiState.relatedBiome?.let { biome ->
								CardWithOverlayLabel(
									painter = rememberAsyncImagePainter(mainBossUiState.relatedBiome.imageUrl),
									content = {
										Row(
											modifier = Modifier.clickable {
												val destination =
													WorldDetailDestination.BiomeDetail(
														biomeId = biome.id
													)
												onItemClick(destination)
											}
										) {
											Box(
												modifier = Modifier.fillMaxHeight()
											) {
												OverlayLabel(
													icon = Lucide.TreePine,
													label = " PRIMARY SPAWN",
												)
											}
											Text(
												biome.name.uppercase(),
												style = MaterialTheme.typography.bodyLarge,
												modifier = Modifier
													.align(Alignment.CenterVertically)
													.fillMaxWidth()
													.padding(8.dp),
												color = Color.White,
												textAlign = TextAlign.Center
											)
										}

									}
								)
							}
							mainBossUiState.relatedForsakenAltar?.let {
								TridentsDividedRow()
								ImageWithTopLabel(
									onItemClick = { pointOfInterestId ->
										val destination =
											WorldDetailDestination.PointOfInterestDetail(
												pointOfInterestId = pointOfInterestId
											)
										onItemClick(destination)
									},
									itemData = mainBossUiState.relatedForsakenAltar,
									horizontalDividerWidth = 250.dp,
									textStyle = MaterialTheme.typography.titleLarge
								)
							}
							SlavicDivider()
							mainBossUiState.relatedSummoningItems.isNotEmpty().let {
								CardWithOverlayLabel(
									height = 180.dp,
									alpha = 0.1f,
									painter = painter,
									content = {
										Column {
											Box(
												modifier = Modifier.fillMaxWidth()
											) {
												OverlayLabel(

													icon = Lucide.Flame,
													label = "SUMMONING ITEMS",
												)
											}
											CustomRowLayout(
												onItemClick = { clickedItemId: String ->
													val material =
														mainBossUiState.relatedSummoningItems.find { it.id == clickedItemId }
													material?.let {
														val destination =
															NavigationHelper.routeToMaterial(
																it.subCategory,
																it.id
															)
														onItemClick(destination)
													}
												},
												relatedSummoningItems = mainBossUiState.relatedSummoningItems,
												modifier = Modifier.weight(1f)
											)

										}
									}
								)
							}
							mainBossUiState.dropItems.isNotEmpty().let {
								HorizontalPagerSection(
									onItemClick = { clickedItemId ->
										val material =
											mainBossUiState.dropItems.find { it.id == clickedItemId }
										material?.let {
											val destination = NavigationHelper.routeToMaterial(
												it.subCategory,
												it.id
											)
											onItemClick(destination)
										}
									},
									list = mainBossUiState.dropItems,
									data = dropData
								)
							}
							GreenTorchesDivider(text = "FORSAKEN POWER")
							mainBossUiState.trophy?.let { trophy ->
								Row(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)
								) {
									Box(
										modifier = Modifier.weight(1f)
									) {
										CardWithTrophy(
											onCardClicked = {
												val destination =
													NavigationHelper.routeToMaterial(
														trophy.subCategory,
														trophy.id
													)
												onItemClick(destination)
											},
											forsakenPower = mainBossUiState.mainBoss.forsakenPower,
											trophyUrl = trophy.imageUrl
										)
									}

									// Move Spacer inside the Row
									Spacer(
										modifier = Modifier.width(15.dp)
									)

									Box(
										modifier = Modifier.weight(1f)
									) {
										mainBossUiState.sacrificialStones?.let { sacrificialStones ->
											CardWithImageAndTitle(
												onCardClick = {
													val destination =
														WorldDetailDestination.PointOfInterestDetail(
															pointOfInterestId = sacrificialStones.id
														)
													onItemClick(destination)
												},
												title = "WHERE TO HANG THE BOSS TROPHY",
												imageUrl = sacrificialStones.imageUrl,
												itemName = sacrificialStones.name,
												contentScale = ContentScale.Crop,
											)
										}
									}
								}
							}
							TridentsDividedRow(text = "BOSS STATS")

							if (mainBossUiState.mainBoss.baseHP.toString().isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									icon = Lucide.Heart,
									label = stringResource(R.string.health),
									value = mainBossUiState.mainBoss.baseHP.toString(),
									expand = {
										isStatInfoExpanded.value = !isStatInfoExpanded.value
									},
									isExpanded = isStatInfoExpanded.value
								)
								AnimatedVisibility(isStatInfoExpanded.value) {
									Text(
										text = "The amount of health points this boss have",
										modifier = Modifier.padding(
											start = BODY_CONTENT_PADDING.dp * 2,
											end = BODY_CONTENT_PADDING.dp
										),
										style = MaterialTheme.typography.bodyLarge
									)
								}
							}

							if (mainBossUiState.mainBoss.baseDamage.isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Swords,
									stringResource(R.string.base_damage),
									"",
									expand = {
										isStatInfoExpanded1.value = !isStatInfoExpanded1.value
									},
									isExpanded = isStatInfoExpanded1.value
								)
								AnimatedVisibility(isStatInfoExpanded1.value) {
									StatColumn(mainBossUiState.mainBoss.baseDamage)
								}
							}
							if (mainBossUiState.mainBoss.weakness.isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Unlink,
									stringResource(R.string.weakness),
									"",
									expand = {
										isStatInfoExpanded2.value = !isStatInfoExpanded2.value
									},
									isExpanded = isStatInfoExpanded2.value
								)
								AnimatedVisibility(isStatInfoExpanded2.value) {
									StatColumn(mainBossUiState.mainBoss.weakness)
								}
							}
							if (mainBossUiState.mainBoss.resistance.isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Grab,
									stringResource(R.string.resistance),
									"",
									expand = {
										isStatInfoExpanded3.value = !isStatInfoExpanded3.value
									},
									isExpanded = isStatInfoExpanded3.value
								)
								AnimatedVisibility(isStatInfoExpanded3.value) {
									StatColumn(mainBossUiState.mainBoss.resistance)
								}
							}
							if (mainBossUiState.mainBoss.collapseImmune.isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									icon = Lucide.Shield,
									label = stringResource(R.string.immune),
									value = "",
									expand = {
										isStatInfoExpanded4.value = !isStatInfoExpanded4.value
									},
									isExpanded = isStatInfoExpanded4.value
								)
								AnimatedVisibility(isStatInfoExpanded4.value) {
									StatColumn(mainBossUiState.mainBoss.collapseImmune)
								}
							}

							SlavicDivider()
							Box(modifier = Modifier.size(70.dp))
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
								isFavorite = mainBossUiState.isFavorite,
								onToggleFavorite = {
									onToggleFavorite(mainBossUiState.mainBoss.toFavorite(), mainBossUiState.isFavorite)
								},
							)
						}
					}

				}

			}

		}
	)
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
	name = "CreatureDetail",
	showBackground = true
)
@Composable
private fun PreviewCreatureDetail() {
	SharedTransitionLayout {
		AnimatedVisibility(visible = true) {
			MainBossContent(
				onBack = { },
				onItemClick = {},
				animatedVisibilityScope = this,
				sharedTransitionScope = this@SharedTransitionLayout,
				mainBossUiState = MainBossDetailUiState(),
				onToggleFavorite = {_,_->{}},
			)
		}
	}

}