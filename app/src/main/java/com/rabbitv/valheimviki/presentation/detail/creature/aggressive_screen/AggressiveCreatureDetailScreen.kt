package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
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
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodDrop
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.navigation.ConsumableDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.viewModel.AggressiveCreatureDetailScreenViewModel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.column.StatColumn
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
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
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
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
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: AggressiveCreatureDetailUiState,
) {

	val pagerState =
		rememberPagerState(pageCount = { uiState.aggressiveCreature?.levels?.size ?: 0 })
	val sharedScrollState = rememberScrollState()
	val isExpandable = remember { mutableStateOf(false) }
	val coroutineScope = rememberCoroutineScope()

	Scaffold { padding ->
		uiState.aggressiveCreature?.let { aggressiveCreature ->
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
							isExpanded = isExpandable,
							boxPadding = BODY_CONTENT_PADDING.dp
						)

						TridentsDividedRow(text = "DETAILS")
						uiState.biome?.let {
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
										WorldDetailDestination.BiomeDetail(biomeId = it.id)
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
											it.name.uppercase(),
											style = MaterialTheme.typography.bodyLarge,
											modifier = Modifier,
											color = Color.White,
											textAlign = TextAlign.Center
										)
									}
								}
							)
						}
						if (uiState.materialDrops.isNotEmpty()) {
							SlavicDivider()
							DroppedItemsSection(
								onItemClick = { clickedItemId, subCategory ->
									val destination =
										NavigationHelper.routeToMaterial(subCategory, clickedItemId)
									onItemClick(destination)
								},
								list = uiState.materialDrops,
								starLevel = pageIndex,
								title = "Drop Items",
								subTitle = "Materials that drop from creature after defeating",
							)
						}

						if (uiState.foodDrops.isNotEmpty()) {
							SlavicDivider()
							DroppedItemsSection(
								onItemClick = { clickedItemId, subCategory ->
									val destination = ConsumableDetailDestination.FoodDetail(
										foodId = clickedItemId,
										category = NavigationHelper.stringToFoodSubCategory(
											subCategory
										)
									)
									onItemClick(destination)
								},
								list = uiState.foodDrops,
								icon = Lucide.Beef,
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
						onToggleFavorite = {
							onToggleFavorite(uiState.aggressiveCreature.toFavorite(), uiState.isFavorite)
						},
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

	val expandedStates = remember {
		mutableStateOf(BooleanArray(5) { false })
	}


	StatCard(
		icon = Lucide.Heart,
		label = stringResource(R.string.health),
		value = currentLevel.baseHp.toString(),
		expandedIndex = 0,
		expandedStates = expandedStates.value,
		onExpandToggle = { index ->
			expandedStates.value = expandedStates.value.copyOf().apply {
				this[index] = !this[index]
			}
		}
	) {
		Text(
			text = "The amount of health points this mob have",
			modifier = Modifier.padding(
				start = BODY_CONTENT_PADDING.dp * 2,
				end = BODY_CONTENT_PADDING.dp
			),
			style = MaterialTheme.typography.bodyLarge
		)
	}

	// Base Damage
	if (creature.baseDamage.isNotBlank() && creature.baseDamage != "null") {
		StatCard(
			icon = Lucide.Swords,
			label = stringResource(R.string.base_damage),
			value = "",
			expandedIndex = 1,
			expandedStates = expandedStates.value,
			onExpandToggle = { index ->
				expandedStates.value = expandedStates.value.copyOf().apply {
					this[index] = !this[index]
				}
			}
		) {
			StatColumn(creature.baseDamage)
		}
	}

	// Weakness
	if (!creature.weakness.isNullOrBlank() && creature.weakness != "null") {
		StatCard(
			icon = Lucide.Unlink,
			label = stringResource(R.string.weakness),
			value = "",
			expandedIndex = 2,
			expandedStates = expandedStates.value,
			onExpandToggle = { index ->
				expandedStates.value = expandedStates.value.copyOf().apply {
					this[index] = !this[index]
				}
			}
		) {
			StatColumn(creature.weakness)
		}
	}

	// Resistance
	if (!creature.resistance.isNullOrBlank() && creature.resistance != "null") {

		StatCard(
			icon = Lucide.Grab,
			label = stringResource(R.string.resistance),
			value = "",
			expandedIndex = 3,
			expandedStates = expandedStates.value,
			onExpandToggle = { index ->
				expandedStates.value = expandedStates.value.copyOf().apply {
					this[index] = !this[index]
				}
			}
		) {
			StatColumn(creature.resistance)
		}

	}

	// Abilities
	if (!creature.abilities.isNullOrBlank() && creature.abilities != "null") {
		StatCard(
			icon = Lucide.Atom,
			label = stringResource(R.string.abilities),
			value = "",
			expandedIndex = 4,
			expandedStates = expandedStates.value,
			onExpandToggle = { index ->
				expandedStates.value = expandedStates.value.copyOf().apply {
					this[index] = !this[index]
				}
			}
		) {
			Text(
				modifier = Modifier.padding(
					start = BODY_CONTENT_PADDING.dp * 2,
					end = BODY_CONTENT_PADDING.dp
				),
				text = creature.abilities,
				style = MaterialTheme.typography.bodyLarge
			)
		}

	}
}

@Composable
private fun StatCard(
	icon: ImageVector,
	label: String,
	value: String,
	expandedIndex: Int,
	expandedStates: BooleanArray,
	onExpandToggle: (Int) -> Unit,
	expandedContent: @Composable () -> Unit
) {
	DarkGlassStatCard(
		modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
		icon = icon,
		label = label,
		value = value,
		expand = { onExpandToggle(expandedIndex) },
		isExpanded = expandedStates[expandedIndex]
	)
	AnimatedVisibility(expandedStates[expandedIndex]) {
		expandedContent()
	}
}

@Composable
fun PageIndicator(
	pagerState: PagerState,
) {
	Row(
		Modifier
			.wrapContentHeight()
			.fillMaxWidth()
			.padding(8.dp),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.Bottom
	) {
		repeat(pagerState.pageCount) { iteration ->
			val color =
				if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
			Box(
				modifier = Modifier
					.padding(2.dp)
					.clip(CircleShape)
					.background(color)
					.size(8.dp)
			)
		}
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
		materialDrops = listOf(
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

		),
		foodDrops = listOf(
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
		),
		isLoading = false,
		error = null
	)


	ValheimVikiAppTheme {
		AggressiveCreatureDetailContent(
			onBack = {},
			onItemClick = {},
			onToggleFavorite = {_,_->{}},
			uiState = exampleUiState
		)
	}
}


//@Preview(name = "AggressiveCreatureDetailScreen")
//@Composable
//private fun PreviewAggressiveCreatureDetailScreen() {
//    AggressiveCreatureDetailContent(
//        uiState = AggressiveCreatureDetailUiState()
//    )
//}