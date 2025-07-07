package com.rabbitv.valheimviki.presentation.detail.food

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Clock2
import com.composables.icons.lucide.CookingPot
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Layers2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.Weight
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.ConsumableDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCardPainter
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.food.viewmodel.FoodDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FoodDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	category: FoodSubCategory,
	viewModel: FoodDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FoodDetailContent(
		uiState = uiState,
		onBack = onBack,
		onItemClick = onItemClick,
		category = category
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FoodDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	uiState: FoodDetailUiState,
	category: FoodSubCategory
) {
	val healingPainter = painterResource(R.drawable.heart_plus)
	val staminaPainter = painterResource(R.drawable.runing)

	val isStatInfoExpanded = remember {
		List(8) { mutableStateOf(false) }
	}
	val scrollState = rememberScrollState()

	val isExpandable = remember { mutableStateOf(false) }

	fun shouldShowValue(value: Any?): Boolean {
		return when (value) {
			null -> false
			is String -> value.isNotBlank()
			is Int -> value != 0
			is Double -> value != 0.0
			is Float -> value != 0f
			else -> true
		}
	}
	BgImage()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentColor = PrimaryWhite
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {

			uiState.food?.let { food ->
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(scrollState)
						.padding(
							top = 20.dp,
							bottom = 70.dp
						),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,

					) {
					FramedImage(food.imageUrl)
					Text(
						food.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					DetailExpandableText(
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						text = food.description,
						isExpanded = isExpandable
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = BODY_CONTENT_PADDING.dp),
						verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
					) {
						if (shouldShowValue(food.health)) {
							SlavicDivider()
							DarkGlassStatCard(
								icon = Lucide.Heart,
								label =  "Health",
								value =  food.health.toString(),
								expand = { isStatInfoExpanded[0].value = !isStatInfoExpanded[0].value },
								isExpanded = isStatInfoExpanded[0].value
							)
							AnimatedVisibility(isStatInfoExpanded[0].value) {
								Text(
									text = "The amount of health points this food adds to your health bar. Health regenerates at 1% per second when above 25% HP. Maximum health is crucial for effective shield use as it directly affects your stagger resistance capacity.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.healing)) {
							DarkGlassStatCardPainter(
								healingPainter,
								"Healing",
								food.healing.toString(),
								expand = { isStatInfoExpanded[1].value = !isStatInfoExpanded[1].value },
								isExpanded = isStatInfoExpanded[1].value
							)
							AnimatedVisibility(isStatInfoExpanded[1].value) {
								Text(
									text = "The rate of health regeneration in HP per second while this food effect is active. This healing occurs continuously throughout the food's duration.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.stamina)) {
							DarkGlassStatCardPainter(
								staminaPainter,
								"Stamina",
								food.stamina.toString(),
								expand = { isStatInfoExpanded[2].value = !isStatInfoExpanded[2].value },
								isExpanded = isStatInfoExpanded[2].value
							)
							AnimatedVisibility(isStatInfoExpanded[2].value) {
								Text(
									text = "The amount of stamina points this food adds to your stamina bar. Stamina is used for running, jumping, attacking, and blocking. Higher stamina allows for longer combat engagements and exploration.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.duration)) {
							DarkGlassStatCard(
								icon = Lucide.Clock2,
								label = "Duration",
								value = "${food.duration.toString()} min",
								expand = { isStatInfoExpanded[3].value = !isStatInfoExpanded[3].value },
								isExpanded = isStatInfoExpanded[3].value
							)
							AnimatedVisibility(isStatInfoExpanded[3].value) {
								Text(
									text = "How long this food's effects remain active after consumption. The timer begins immediately upon eating and cannot be paused or extended.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.eitr)) {
							DarkGlassStatCard(
								icon = 	Lucide.Wand,
								label = "Eitr",
								value =  food.eitr.toString(),
								expand = { isStatInfoExpanded[4].value = !isStatInfoExpanded[4].value },
								isExpanded = isStatInfoExpanded[4].value
							)
							AnimatedVisibility(isStatInfoExpanded[4].value) {
								Text(
									text = "The amount of eitr (magic energy) this food provides. Eitr is required for casting magic spells and using staffs. Only certain foods provide this mystical resource.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.weight)) {
							DarkGlassStatCard(
								icon = 	Lucide.Weight,
								label = "Weight",
								value = food.weight.toString(),
								expand = { isStatInfoExpanded[5].value = !isStatInfoExpanded[5].value },
								isExpanded = isStatInfoExpanded[5].value
							)
							AnimatedVisibility(isStatInfoExpanded[5].value) {
								Text(
									text = "The weight of one unit of this food in your inventory. Total weight affects movement speed when overencumbered.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.forkType)) {
							DarkGlassStatCard(
								icon = Lucide.Info,
								label =  "Fork Type",
								value =  food.forkType.toString(),
								expand = { isStatInfoExpanded[6].value = !isStatInfoExpanded[6].value },
								isExpanded = isStatInfoExpanded[6].value
							)
							AnimatedVisibility(isStatInfoExpanded[6].value) {
								Text(
									text = "The fork icon color indicates this food's primary benefit: Red fork for health-focused foods, yellow fork for stamina-focused foods, blue fork for eitr-focused foods, and white fork for balanced foods that provide equal benefits to multiple stats.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
						if (shouldShowValue(food.stackSize)) {
							DarkGlassStatCard(
								icon = 	Lucide.Layers2,
								label = "Stack Size",
								value =  food.stackSize.toString(),
								expand = { isStatInfoExpanded[7].value = !isStatInfoExpanded[7].value },
								isExpanded = isStatInfoExpanded[7].value
							)
							AnimatedVisibility(isStatInfoExpanded[7].value) {
								Text(
									text = "The maximum number of this food item that can be stored in a single inventory slot. Higher stack sizes save valuable inventory space during long expeditions.",
									modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}

					}
					uiState.craftingCookingStation?.let { craftingStation ->
						SlavicDivider()
						CardImageWithTopLabel(
							onClickedItem ={
								val destination = BuildingDetailDestination.CraftingObjectDetail(
									craftingObjectId = craftingStation.id
								)
								onItemClick(destination)
							},
							itemData = craftingStation,
							subTitle = if (category == FoodSubCategory.UNCOOKED_FOOD) "Cook at Station to Consume" else "Requires Cooking Station",
							contentScale = ContentScale.Fit,
							painter = painterResource(R.drawable.food_bg)
						)
					}
					if (uiState.foodForRecipe.isNotEmpty() || uiState.materialsForRecipe.isNotEmpty()) {
						TridentsDividedRow()
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp)
						) {
							SectionHeader(
								data = HorizontalPagerWithHeaderData(
									title = "Recipe",
									subTitle = "Ingredients required to craft this item",
									icon = Lucide.CookingPot,
									iconRotationDegrees = 0f,
									contentScale = ContentScale.Crop,
									starLevelIndex = 0,
								),
								modifier = Modifier,
							)
						}

						Spacer(modifier = Modifier.padding(6.dp))
						TwoColumnGrid {
							for (item in uiState.materialsForRecipe) {
								CustomItemCard(
									onItemClick = {
										val destination =
											NavigationHelper.routeToMaterial(item.itemDrop.subCategory, item.itemDrop.id)
										onItemClick(destination)
									},
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = item.itemDrop.imageUrl,
									name = item.itemDrop.name,
									quantity = item.quantityList.firstOrNull()
								)
							}
							for (item in uiState.foodForRecipe) {
								CustomItemCard(
									onItemClick = {
										val subCategory = NavigationHelper.stringToFoodSubCategory(item.itemDrop.subCategory)
										val destination = ConsumableDetailDestination.FoodDetail(item.itemDrop.id,subCategory)
										onItemClick(destination)
									},
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = item.itemDrop.imageUrl,
									name = item.itemDrop.name,
									quantity = item.quantityList.firstOrNull()
								)
							}
						}
					}

				}
			}
			AnimatedBackButton(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp),
				scrollState = scrollState,
				onBack = onBack
			)
		}
	}
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview("FoodDetailContentPreview", showBackground = true)
@Composable
fun PreviewFoodDetailContentCooked() {
	val fakeFood = Food(
		id = "serpent_stew",
		imageUrl = "https://example.com/images/serpent_stew.png",
		category = "Food",
		subCategory = "Cooked",
		name = "Serpent Stew",
		description = "A rich stew made from serpent meat. Greatly increases health and stamina.",
		order = 1,
		eitr = 0,
		health = 80,
		weight = 1.0,
		healing = 4,
		stamina = 26,
		duration = "1600:00",
		forkType = "Blue",
		stackSize = 10
	)
	val fakeMaterial = FakeData.generateFakeMaterials()[0]
	val fakeFoodList = listOf(
		RecipeFoodData(
			itemDrop = fakeFood,
			quantityList = listOf(1, 2, 3),
			chanceStarList = listOf(100, 75, 50)
		),
		RecipeFoodData(
			itemDrop = fakeFood,
			quantityList = listOf(2, 3, 4),
			chanceStarList = listOf(90, 70, 40)
		),
		RecipeFoodData(
			itemDrop = fakeFood,
			quantityList = listOf(1, 1, 2),
			chanceStarList = listOf(80, 60, 30)
		)
	)

	val fakeMaterialsList = listOf(
		RecipeMaterialData(
			itemDrop = fakeMaterial,
			quantityList = listOf(3, 4, 5),
			chanceStarList = listOf(100, 85, 60)
		),
		RecipeMaterialData(
			itemDrop = fakeMaterial,
			quantityList = listOf(2, 3, 4),
			chanceStarList = listOf(95, 70, 45)
		),
		RecipeMaterialData(
			itemDrop = fakeMaterial,
			quantityList = listOf(1, 2, 3),
			chanceStarList = listOf(85, 65, 40)
		)
	)


	val craftingStation = CraftingObject(
		id = "workbench",
		imageUrl = "https://example.com/images/workbench.png",
		category = "Crafting Station",
		subCategory = "Basic",
		name = "Workbench",
		description = "Used for crafting basic tools, weapons, and building materials.",
		order = 1
	)

	ValheimVikiAppTheme {
		FoodDetailContent(
			onBack = {},
			onItemClick = {},
			uiState = FoodDetailUiState(
				food = fakeFood,
				craftingCookingStation = craftingStation,
				foodForRecipe = fakeFoodList,
				materialsForRecipe = fakeMaterialsList,
				isLoading = false,
				error = null
			),
			category = FoodSubCategory.COOKED_FOOD,
		)
	}

}
