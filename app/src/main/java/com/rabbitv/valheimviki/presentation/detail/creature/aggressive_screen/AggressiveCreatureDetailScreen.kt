package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Atom
import com.composables.icons.lucide.Beef
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Trophy
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodDrop
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.animated_stat_card.AnimatedStatCard
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.page_indicator.PageIndicator
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureUiEvent
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.viewModel.AggressiveCreatureDetailScreenViewModel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.column.StatColumn
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StarLevelRow
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch

@Composable
fun AggressiveCreatureDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: AggressiveCreatureDetailScreenViewModel = hiltViewModel()
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(AggressiveCreatureUiEvent.ToggleFavorite)
	}
	AggressiveCreatureDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}


@Composable
fun AggressiveCreatureDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: AggressiveCreatureDetailUiState,
) {

	val pagerState =
		rememberPagerState(pageCount = { uiState.aggressiveCreature?.levels?.size ?: 0 })
	val sharedScrollState = rememberScrollState()
	val coroutineScope = rememberCoroutineScope()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	Scaffold { padding ->
		uiState.aggressiveCreature?.let { aggressiveCreature ->
			HorizontalPager(
				state = pagerState,
				modifier = Modifier
					.padding(padding)
					.fillMaxWidth(),
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
						val currentLevel = aggressiveCreature.levels[pageIndex]
						MainDetailImage(
							imageUrl = aggressiveCreature.levels[pageIndex].image.toString(),
							name = aggressiveCreature.name,
							textAlign = TextAlign.Center
						)
						PageIndicator(pagerState)
						StarLevelRow(
							levelsNumber = aggressiveCreature.levels.size,
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
							text = aggressiveCreature.description,
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
							divider = { SlavicDivider() },
							state = uiState.materialDrops,
						) { state ->
							DroppedItemsSection(
								modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp),
								onItemClick = handleClick,
								list = state,
								starLevel = pageIndex,
								title = "Drop Items",
								subTitle = "Materials that drop from creature after defeating",
								icon = { Lucide.Trophy }
							)
						}

						UiSection(
							divider = { SlavicDivider() },
							state = uiState.foodDrops,
						) { state ->
							DroppedItemsSection(
								onItemClick = handleClick,
								list = state,
								icon = { Lucide.Beef },
								starLevel = pageIndex,
								title = "Food Drops",
								subTitle = "Food items that drop from creature and can be instantly eaten",
							)
						}

						TridentsDividedRow(text = "MOB STATS")
						CreatureStatsSection(
							creature = aggressiveCreature,
							currentLevel = currentLevel
						)

						SlavicDivider()
						Box(modifier = Modifier.size(70.dp))
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

@Composable
private fun CreatureStatsSection(
	creature: AggressiveCreature,
	currentLevel: LevelCreatureData
) {
    AnimatedStatCard(
        id = "aggr_health_stat",
        icon = Lucide.Heart,
        label = stringResource(R.string.health),
        value = currentLevel.baseHp.toString(),
        details = "The amount of health points this mob have",
    )

	// Base Damage
    if (creature.baseDamage.isNotBlank() && creature.baseDamage != "null") {
        AnimatedStatCard(
            id = "aggr_base_damage_stat",
            icon = Lucide.Swords,
            label = stringResource(R.string.base_damage),
            value = "",
            details = creature.baseDamage,
            isStatColumn = true,
        )
    }

	// Weakness
    if (!creature.weakness.isNullOrBlank() && creature.weakness != "null") {
        AnimatedStatCard(
            id = "aggr_weakness_stat",
            icon = Lucide.Unlink,
            label = stringResource(R.string.weakness),
            value = "",
            details = creature.weakness,
            isStatColumn = true,
        )
    }

	// Resistance
    if (!creature.resistance.isNullOrBlank() && creature.resistance != "null") {
        AnimatedStatCard(
            id = "aggr_resistance_stat",
            icon = Lucide.Grab,
            label = stringResource(R.string.resistance),
            value = "",
            details = creature.resistance,
            isStatColumn = true,
        )

    }

	// Abilities
    if (!creature.abilities.isNullOrBlank() && creature.abilities != "null") {
        AnimatedStatCard(
            id = "aggr_abilities_stat",
            icon = Lucide.Atom,
            label = stringResource(R.string.abilities),
            value = "",
            details = creature.abilities,
        )

    }
}

 


@Preview(name = "CreaturePage")
@Composable
fun PreviewCreaturePage() {
	val ag = AggressiveCreature(
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
	val exampleUiState = AggressiveCreatureDetailUiState(
		aggressiveCreature = ag,
		biome = Biome(
			id = "meadows",
			category = "peaceful",
			imageUrl = "https://example.com/biome/meadows.png",
			name = "Meadows",
			description = "A calm and peaceful biome with lush fields and gentle hills.",
			order = 1
		),
		materialDrops = UIState.Success(
			listOf(
				MaterialDrop(
					itemDrop = Material(
						id = "troll_hide",
						category = "resource",
						imageUrl = "https://example.com/material/troll_hide.png",
						name = "Troll Hide",
						description = "Thick and durable hide dropped by trolls.",
						order = 5,
						subCategory = "",
						usage = "",
						growthTime = "",
						needCultivatorGround = "",
						price = 332,
						effect = "",
						sellPrice = 2,
						subType = ""
					),
					quantityList = listOf(3, 5, 7),
					chanceStarList = listOf(100, 75, 50)
				),
				MaterialDrop(
					itemDrop = Material(
						id = "troll_hide",
						category = "resource",
						imageUrl = "https://example.com/material/troll_hide.png",
						name = "Troll Hide",
						description = "Thick and durable hide dropped by trolls.",
						order = 5,
						subCategory = "",
						usage = "",
						growthTime = "",
						needCultivatorGround = "",
						price = 332,
						effect = "",
						sellPrice = 2,
						subType = ""
					),
					quantityList = listOf(3, 5, 7),
					chanceStarList = listOf(100, 75, 50)
				),
				MaterialDrop(
					itemDrop = Material(
						id = "troll_hide",
						category = "resource",
						imageUrl = "https://example.com/material/troll_hide.png",
						name = "Troll Hide",
						description = "Thick and durable hide dropped by trolls.",
						order = 5,
						subCategory = "",
						usage = "",
						growthTime = "",
						needCultivatorGround = "",
						price = 332,
						effect = "",
						sellPrice = 2,
						subType = ""
					),
					quantityList = listOf(3, 5, 7),
					chanceStarList = listOf(100, 75, 50)
				)
			)

		),
		foodDrops = UIState.Success(
			listOf(
				FoodDrop(
					itemDrop = Food(
						id = "raw_meat",
						category = "food",
						imageUrl = "https://example.com/food/raw_meat.png",
						name = "Raw Meat",
						description = "Uncooked meat dropped by wild creatures.",
						order = 2,
						health = 20,
						stamina = 15,
						duration = "20:00",
						healing = 2,
						subCategory = "",
						eitr = 2,
						weight = 0.2,
						forkType = "",
						stackSize = 23,
					),
					quantityList = listOf(1, 2, 3),
					chanceStarList = listOf(100, 80, 60)
				)
			)
		)
	)


	ValheimVikiAppTheme {
		AggressiveCreatureDetailContent(
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { },
			uiState = exampleUiState
		)
	}
}

