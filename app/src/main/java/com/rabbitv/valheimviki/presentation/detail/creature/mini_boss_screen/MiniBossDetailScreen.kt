package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.animated_stat_card.AnimatedStatCard
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model.MiniBossDetailUIEvent
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
	val onToggleFavorite = {
		viewModel.uiEvent(MiniBossDetailUIEvent.ToggleFavorite)
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
	onToggleFavorite: () -> Unit,
	miniBossUiSate: MiniBossDetailUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()

	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val dropItemData = remember {
		HorizontalPagerData(
			title = "Drop Items",
			subTitle = "Items that drop from boss after defeating him",
			icon = Lucide.Trophy,
			iconRotationDegrees = 0f,
			itemContentScale = ContentScale.Crop,
		)
	}

	Scaffold(
		content = { padding ->
			BgImage()
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding)
			) {
				if (miniBossUiSate.miniBoss != null) {
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
								modifier = Modifier.align(Alignment.CenterHorizontally),
								text = "PRIMARY SPAWN",
								textAlign = TextAlign.Center,
								style = MaterialTheme.typography.titleLarge,
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
						UiSection(
							state = miniBossUiSate.dropItems
						) { data ->
							HorizontalPagerSection(
								onItemClick = handleClick,
								list = data,
								data = dropItemData
							)
						}
						TridentsDividedRow(text = "BOSS STATS")

						if (miniBossUiSate.miniBoss.baseHP.toString().isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "mini_boss_health_stat",
								icon = Lucide.Heart,
								label = "Health",
								value = miniBossUiSate.miniBoss.baseHP.toString(),
								details = "The amount of health points this boss have",
							)
						}

						if (miniBossUiSate.miniBoss.baseDamage.isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "mini_boss_base_damage_stat",
								icon = Lucide.Swords,
								label = stringResource(R.string.base_damage),
								value = "",
								details = miniBossUiSate.miniBoss.baseDamage,
								isStatColumn = true,
							)
						}
						if (!miniBossUiSate.miniBoss.weakness.isNullOrBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "mini_boss_weakness_stat",
								icon = Lucide.Unlink,
								label = stringResource(R.string.weakness),
								value = "",
								details = miniBossUiSate.miniBoss.weakness,
								isStatColumn = true,
							)
						}
						if (!miniBossUiSate.miniBoss.resistance.isNullOrBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "mini_boss_resistance_stat",
								icon = Lucide.Grab,
								label = stringResource(R.string.resistance),
								value = "",
								details = miniBossUiSate.miniBoss.resistance,
								isStatColumn = true,
							)
						}
						if (!miniBossUiSate.miniBoss.collapseImmune.isNullOrBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "mini_boss_immune_stat",
								icon = Lucide.Shield,
								label = stringResource(R.string.immune),
								value = "",
								details = miniBossUiSate.miniBoss.collapseImmune,
								isStatColumn = true,
							)
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
							onToggleFavorite = { onToggleFavorite() },
						)
					}
				}
			}
		}
	)
}

