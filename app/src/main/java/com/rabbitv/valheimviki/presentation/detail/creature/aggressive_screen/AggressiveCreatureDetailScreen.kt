package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Beef
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodDrop
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model.AggressiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.viewModel.AggressiveCreatureDetailScreenViewModel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardStatDetails
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StarLevelRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StatsFlowRow
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch

@Composable
fun AggressiveCreatureDetailScreen(
	onBack: () -> Unit,
	viewModel: AggressiveCreatureDetailScreenViewModel = hiltViewModel()
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	AggressiveCreatureDetailContent(
		uiState = uiState,
		onBack = onBack,
	)

}


@Composable
fun AggressiveCreatureDetailContent(
	onBack: () -> Unit,
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
				beyondViewportPageCount = aggressiveCreature.levels.size,
			) { pageIndex ->
				Column(
					modifier = Modifier.verticalScroll(sharedScrollState),
					verticalArrangement = Arrangement.Center,
				) {
					MainDetailImage(
						onBack = onBack,
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
						isExpanded = isExpandable
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
							list = uiState.materialDrops,
							starLevel = pageIndex,
							title = "Drop Items",
							subTitle = "Materials that drop from creature after defeating",
						)
					}

					if (uiState.foodDrops.isNotEmpty()) {
						SlavicDivider()
						DroppedItemsSection(
							list = uiState.foodDrops,
							icon = Lucide.Beef,
							starLevel = pageIndex,
							title = "Food Drops",
							subTitle = "Food items that drop from creature and can be instanly eaten",
						)
					}

					TridentsDividedRow(text = "BOSS STATS")
					Box(
						modifier = Modifier.padding(horizontal = 10.dp)
					)
					{
						CardStatDetails(
							title = stringResource(R.string.baseHp),
							text = uiState.aggressiveCreature.levels[pageIndex].baseHp.toString(),
							icon = Icons.Outlined.Favorite,
							iconColor = Color.Red,
							styleTextFirst = MaterialTheme.typography.labelSmall,
							styleTextSecond = MaterialTheme.typography.bodyLarge,
							iconSize = 36.dp
						)
					}


					StatsFlowRow(
						baseDamage = uiState.aggressiveCreature.levels[pageIndex].baseDamage,
						weakness = uiState.aggressiveCreature.weakness,
						resistance = uiState.aggressiveCreature.resistance,
						abilities = uiState.aggressiveCreature.abilities,
					)
					SlavicDivider()
					Box(modifier = Modifier.size(45.dp))
				}
			}
		}
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