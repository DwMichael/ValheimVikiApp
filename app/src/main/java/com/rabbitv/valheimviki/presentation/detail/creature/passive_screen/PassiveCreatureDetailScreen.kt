package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Atom
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.page_indicator.PageIndicator
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardStatDetails
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StarLevelRow
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.viewmodel.PassiveCreatureDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.launch


@Composable
fun PassiveCreatureDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: PassiveCreatureDetailScreenViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(PassiveCreatureDetailUiEvent.ToggleFavorite)
	}
	PassiveCreatureDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,

		)

}


@Composable
fun PassiveCreatureDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: PassiveCreatureDetailUiState,
) {


	val pagerState =
		rememberPagerState(
			pageCount = { uiState.passiveCreature?.levels?.size ?: 0 },
		)

	val sharedScrollState = rememberScrollState()
	val coroutineScope = rememberCoroutineScope()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	Scaffold { padding ->
		uiState.passiveCreature?.let { passiveCreature ->
			HorizontalPager(
				state = pagerState,
				modifier = Modifier
					.padding(padding)
					.fillMaxWidth(),
				beyondViewportPageCount = 1,
			) { pageIndex ->
				Box(
					modifier = Modifier
						.fillMaxSize()
				) {
					Column(
						modifier = Modifier.verticalScroll(sharedScrollState),
						verticalArrangement = Arrangement.Top,
						horizontalAlignment = Alignment.Start,
					) {
						MainDetailImage(
							imageUrl = passiveCreature.levels[pageIndex].image.toString(),
							name = passiveCreature.name,
							textAlign = TextAlign.Center
						)
						PageIndicator(pagerState)
						StarLevelRow(
							levelsNumber = passiveCreature.levels.size,
							pageIndex = pageIndex,
							paddingValues = PaddingValues(BODY_CONTENT_PADDING.dp),
							onClick = {
								coroutineScope.launch {
									pagerState.animateScrollToPage(it)
								}
							}
						)
						HorizontalDivider(
							modifier = Modifier
								.fillMaxWidth()
								.padding(BODY_CONTENT_PADDING.dp),
							thickness = 1.dp,
							color = PrimaryWhite
						)
						DetailExpandableText(
							text = passiveCreature.description,
							collapsedMaxLine = 3,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
						TridentsDividedRow(text = "DETAILS")
						uiState.biome?.let { biome ->
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
										WorldDetailDestination.BiomeDetail(
											biomeId = biome.id,
											imageUrl = biome.imageUrl,
											title = biome.name
										)
									onItemClick(destination)
								},
								painter = rememberAsyncImagePainter(uiState.biome.imageUrl),
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
						UiSection(
							state = uiState.materialDrops,
							divider = { SlavicDivider() }
						) { data ->
							DroppedItemsSection(
								list = data,
								starLevel = pageIndex,
								title = "Drop Items",
								subTitle = "Materials that drop from creature after defeating",
								onItemClick = handleClick,
								icon = { Lucide.Trophy },
							)
						}



						TridentsDividedRow(text = "BOSS STATS")
						CardStatDetails(
							title = stringResource(R.string.baseHp),
							text = uiState.passiveCreature.levels[pageIndex].baseHp.toString(),
							icon = Lucide.Heart,
							iconColor = Color.Red,
							styleTextFirst = MaterialTheme.typography.labelSmall,
							styleTextSecond = MaterialTheme.typography.bodyLarge,
							iconSize = 36.dp,
							cardPadding = BODY_CONTENT_PADDING.dp
						)

						CardStatDetails(
							title = stringResource(R.string.fun_fact),
							text = uiState.passiveCreature.abilities.toString(),
							icon = Lucide.Atom,
							iconColor = Color.White,
							styleTextFirst = MaterialTheme.typography.labelSmall,
							styleTextSecond = MaterialTheme.typography.bodyMedium,
							iconSize = 36.dp,
							cardPadding = BODY_CONTENT_PADDING.dp
						)
						if (uiState.passiveCreature.notes.isNotBlank()) {
							SlavicDivider()
							Text(
								modifier = Modifier
									.align(Alignment.CenterHorizontally)
									.padding(horizontal = BODY_CONTENT_PADDING.dp),
								text = "NOTE",
								textAlign = TextAlign.Center,
								style = MaterialTheme.typography.headlineMedium,
							)
							DetailExpandableText(
								text = passiveCreature.notes,
								collapsedMaxLine = 3,
								boxPadding = BODY_CONTENT_PADDING.dp
							)
						}
						SlavicDivider()
						Box(modifier = Modifier.size(45.dp))
					}
					AnimatedBackButton(
						modifier = Modifier
							.align(Alignment.TopStart)
							.padding(16.dp),
						scrollState = sharedScrollState,
						onBack = onBack
					)
					FavoriteButton(
						modifier = Modifier
							.align(Alignment.TopEnd)
							.padding(16.dp),
						isFavorite = uiState.isFavorite,
						onToggleFavorite = { onToggleFavorite() },
					)
				}
			}
		}
	}
}


@Preview(name = "CreaturePage")
@Composable
fun PreviewCreaturePage() {
	AggressiveCreature(
		id = "1",
		category = "asd",
		subCategory = "sdasd",
		imageUrl = "sadasd",
		name = "sadsdd",
		description = "asdasd2",
		order = 2,
		weakness = "SDASD",
		resistance = "dasdas2",
		baseDamage = "dsasdasd",
		levels = listOf(
			LevelCreatureData(
				level = 1,
				baseHp = 23123,
				baseDamage = "DAsdasd",
				image = "dsadasd"
			)
		),
		abilities = "SDASDAD"
	)
	remember { mutableStateOf(true) }

}

