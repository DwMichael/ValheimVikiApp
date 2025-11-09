package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.TreePine
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.Trophy
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.animated_stat_card.AnimatedStatCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.GreenTorchesDivider
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedGrid
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedItems
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithImageAndTitle
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithTrophy
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossUiEvent
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.viewmodel.MainBossScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MainBossScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val mainBossUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(MainBossUiEvent.ToggleFavorite)
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
	onToggleFavorite: () -> Unit,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
	mainBossUiState: MainBossDetailUiState,
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val dropData = HorizontalPagerData(
		title = "Drop Items",
		subTitle = "Items that drop from boss after defeating him",
		icon = Lucide.Trophy,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)




	Scaffold(
		content = { padding ->
			if (mainBossUiState.mainBoss != null) {
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
						horizontalAlignment = Alignment.CenterHorizontally,
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
										modifier = Modifier.clickable { handleClick(biome) }
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
								onItemClick = handleClick,
								itemData = mainBossUiState.relatedForsakenAltar,
								horizontalDividerWidth = 250.dp,
								textStyle = MaterialTheme.typography.titleLarge
							)
						}
						SlavicDivider()
						UiSection(
							state = mainBossUiState.relatedSummoningItems
						) { data ->
							SectionHeader(
								modifier = Modifier
									.fillMaxWidth()
									.wrapContentHeight()
									.padding(horizontal = BODY_CONTENT_PADDING.dp),
								data = SectionHeaderData(
									"SUMMONING ITEMS",
									"Items needed to summon boss",
									Lucide.TrendingUp
								),
							)
							NestedGrid(
								nestedItems = NestedItems(items = data),
							) { product ->
								CustomItemCard(
									itemData = product.itemDrop,
									onItemClick = handleClick,
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = product.itemDrop.imageUrl,
									name = product.itemDrop.name,
									quantity = product.quantityList.firstOrNull()
								)
							}
						}
						UiSection(
							state = mainBossUiState.dropItems
						) { data ->
							HorizontalPagerSection(
								onItemClick = handleClick,
								list = data,
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
										onCardClicked = { handleClick(trophy) },
										forsakenPower = mainBossUiState.mainBoss.forsakenPower,
										trophyUrl = trophy.imageUrl
									)
								}

								Spacer(
									modifier = Modifier.width(15.dp)
								)

								Box(
									modifier = Modifier.weight(1f)
								) {
									mainBossUiState.sacrificialStones?.let { sacrificialStones ->
										CardWithImageAndTitle(
											onCardClick = { handleClick(sacrificialStones) },
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
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "heart_stat",
								icon = Lucide.Heart,
								label = stringResource(R.string.health),
								value = mainBossUiState.mainBoss.baseHP.toString(),
								details = stringResource(R.string.what_is_health),
							)
						}

						if (mainBossUiState.mainBoss.baseDamage.isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "base_damage_stat",
								icon = Lucide.Swords,
								label = stringResource(R.string.base_damage),
								value = "",
								details = mainBossUiState.mainBoss.baseDamage,
								isStatColumn = true,
							)
						}
						if (mainBossUiState.mainBoss.weakness.isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "swords_stat",
								icon = Lucide.Unlink,
								label = stringResource(R.string.weakness),
								value = "",
								details = mainBossUiState.mainBoss.weakness,
								isStatColumn = true,
							)
						}
						if (mainBossUiState.mainBoss.resistance.isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "resistance_stat",
								icon = Lucide.Grab,
								label = stringResource(R.string.resistance),
								value = "",
								details = mainBossUiState.mainBoss.resistance,
								isStatColumn = true,
							)
						}
						if (mainBossUiState.mainBoss.collapseImmune.isNotBlank()) {
							AnimatedStatCard(
								modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
								id = "collapse_immune_stat",
								icon = Lucide.Shield,
								label = stringResource(R.string.immune),
								value = "",
								details = mainBossUiState.mainBoss.collapseImmune,
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
							isFavorite = mainBossUiState.isFavorite,
							onToggleFavorite = { onToggleFavorite() },
						)
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
				mainBossUiState = MainBossDetailUiState(
					mainBoss = MainBoss(
						id = "elder",
						category = "creature",
						subCategory = "main_boss",
						imageUrl = "https://via.placeholder.com/512",
						name = "The Elder",
						description = "Ancient guardian of the Black Forest.",
						order = 2,
						baseHP = 2500,
						weakness = "Fire",
						resistance = "Pierce",
						baseDamage = "Melee smash; Root summon",
						collapseImmune = "Stagger",
						forsakenPower = "Eikthyr's Speed"
					)
				),
				onToggleFavorite = { },
			)
		}
	}

}