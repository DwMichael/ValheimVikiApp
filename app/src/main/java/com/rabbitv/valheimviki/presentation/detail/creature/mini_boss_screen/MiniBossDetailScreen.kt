package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Trophy
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.column.StatColumn
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model.MiniBossDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.viewmodel.MiniBossDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MiniBossDetailScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val miniBossUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}
	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")

	MiniBossContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		miniBossUiSate = miniBossUiState,
		sharedTransitionScope = sharedTransitionScope,
		animatedVisibilityScope = animatedVisibilityScope
	)
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	miniBossUiSate: MiniBossDetailUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val isStatInfoExpanded = remember {
		List(5) { mutableStateOf(false) }
	}
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val dropItemData = HorizontalPagerData(
		title = "Drop Items",
		subTitle = "Items that drop from boss after defeating him",
		icon = Lucide.Trophy,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop,
	)

	Scaffold(
		content = { padding ->
			AnimatedContent(
				targetState = miniBossUiSate.isLoading,
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
				} else if (miniBossUiSate.miniBoss != null) {
					BgImage()
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(padding)
					) {
						Column(
							modifier = Modifier
								.testTag("MiniBossDetailScreen")
								.fillMaxSize()
								.verticalScroll(scrollState, enabled = !isRunning),
							verticalArrangement = Arrangement.Top,
							horizontalAlignment = Alignment.Start,
						) {
							MainDetailImageAnimated(
								sharedTransitionScope = sharedTransitionScope,
								animatedVisibilityScope = animatedVisibilityScope,
								textAlign = TextAlign.Center,
								id = miniBossUiSate.miniBoss.id,
								imageUrl = miniBossUiSate.miniBoss.imageUrl,
								title = miniBossUiSate.miniBoss.name
							)
							DetailExpandableText(
								text = miniBossUiSate.miniBoss.description,
								boxPadding = BODY_CONTENT_PADDING.dp
							)
							TridentsDividedRow(text = "BOSS DETAIL")
							miniBossUiSate.primarySpawn?.let { primarySpawn ->
								Text(
									modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
									text = "PRIMARY SPAWN",
									textAlign = TextAlign.Left,
									style = MaterialTheme.typography.titleSmall,
									maxLines = 1,
									overflow = TextOverflow.Visible
								)
								CardWithOverlayLabel(
									onClickedItem = {
										val destination =
											WorldDetailDestination.PointOfInterestDetail(
												pointOfInterestId = primarySpawn.id
											)
										onItemClick(destination)
									},
									painter = rememberAsyncImagePainter(miniBossUiSate.primarySpawn.imageUrl),
									content = {
										Box(
											modifier = Modifier
												.fillMaxSize()
												.wrapContentHeight(Alignment.CenterVertically)
												.wrapContentWidth(Alignment.CenterHorizontally)
										) {
											Text(
												primarySpawn.name.uppercase(),
												style = MaterialTheme.typography.bodyLarge,
												modifier = Modifier,
												color = Color.White,
												textAlign = TextAlign.Center
											)
										}
									}
								)
							}
							if (miniBossUiSate.dropItems.isNotEmpty()) {
								SlavicDivider()
								HorizontalPagerSection(
									onItemClick = handleItemClick,
									list = miniBossUiSate.dropItems,
									data = dropItemData
								)
							}
							if (miniBossUiSate.dropItems.isEmpty() && miniBossUiSate.primarySpawn == null) {
								Row(modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp))
								{
									Spacer(Modifier.weight(1f))
									Text(
										"Connect to Internet to fetch boss detail data",
										style = MaterialTheme.typography.bodyLarge,
										modifier = Modifier.weight(2f),
										color = Color.White,
										textAlign = TextAlign.Center
									)
									Spacer(Modifier.weight(1f))
								}

							}
							TridentsDividedRow(text = "BOSS STATS")

							if (miniBossUiSate.miniBoss.baseHP.toString().isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									icon = Lucide.Heart,
									label = "Health",
									value = miniBossUiSate.miniBoss.baseHP.toString(),
									expand = {
										isStatInfoExpanded[0].value = !isStatInfoExpanded[0].value
									},
									isExpanded = isStatInfoExpanded[0].value
								)
								AnimatedVisibility(isStatInfoExpanded[0].value) {
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

							if (miniBossUiSate.miniBoss.baseDamage.isNotBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Swords,
									stringResource(R.string.base_damage),
									"",
									expand = {
										isStatInfoExpanded[1].value = !isStatInfoExpanded[1].value
									},
									isExpanded = isStatInfoExpanded[1].value
								)
								AnimatedVisibility(isStatInfoExpanded[1].value) {
									StatColumn(miniBossUiSate.miniBoss.baseDamage)
								}
							}
							if (!miniBossUiSate.miniBoss.weakness.isNullOrBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Unlink,
									stringResource(R.string.weakness),
									"",
									expand = {
										isStatInfoExpanded[2].value = !isStatInfoExpanded[2].value
									},
									isExpanded = isStatInfoExpanded[2].value
								)
								AnimatedVisibility(isStatInfoExpanded[2].value) {
									StatColumn(miniBossUiSate.miniBoss.weakness)
								}
							}
							if (!miniBossUiSate.miniBoss.resistance.isNullOrBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									Lucide.Grab,
									stringResource(R.string.resistance),
									"",
									expand = {
										isStatInfoExpanded[3].value = !isStatInfoExpanded[3].value
									},
									isExpanded = isStatInfoExpanded[3].value
								)
								AnimatedVisibility(isStatInfoExpanded[3].value) {
									StatColumn(miniBossUiSate.miniBoss.resistance)
								}
							}
							if (!miniBossUiSate.miniBoss.collapseImmune.isNullOrBlank()) {
								DarkGlassStatCard(
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									icon = Lucide.Shield,
									label = stringResource(R.string.immune),
									value = "",
									expand = {
										isStatInfoExpanded[4].value = !isStatInfoExpanded[4].value
									},
									isExpanded = isStatInfoExpanded[4].value
								)
								AnimatedVisibility(isStatInfoExpanded[4].value) {
									StatColumn(miniBossUiSate.miniBoss.collapseImmune)
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
								isFavorite = miniBossUiSate.isFavorite,
								onToggleFavorite = {
									onToggleFavorite(
										miniBossUiSate.miniBoss.toFavorite(),
										miniBossUiSate.isFavorite
									)
								},
							)
						}
					}
				}
			}

		}
	)
}

