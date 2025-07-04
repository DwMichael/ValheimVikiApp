package com.rabbitv.valheimviki.presentation.detail.food

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCardPainter
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.food.viewmodel.FoodDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FoodDetailScreen(
	onBack: () -> Unit,
	category: FoodSubCategory,
	viewModel: FoodDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FoodDetailContent(
		uiState = uiState,
		onBack = onBack,
		category = category
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FoodDetailContent(
	uiState: FoodDetailUiState,
	onBack: () -> Unit,
	category: FoodSubCategory
) {
	val healingPainter = painterResource(R.drawable.heart_plus)
	val staminaPainter = painterResource(R.drawable.runing)


	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isStatInfoExpanded2 = remember { mutableStateOf(false) }
	val isStatInfoExpanded3 = remember { mutableStateOf(false) }
	val isStatInfoExpanded4 = remember { mutableStateOf(false) }
	val isStatInfoExpanded5 = remember { mutableStateOf(false) }
	val isStatInfoExpanded6 = remember { mutableStateOf(false) }
	val isStatInfoExpanded7 = remember { mutableStateOf(false) }
	val isStatInfoExpanded8 = remember { mutableStateOf(false) }

	val scrollState = rememberScrollState()
	val previousScrollValue = remember { mutableIntStateOf(0) }

	val recipeItems: List<Droppable> = uiState.materialsForRecipe + uiState.foodForRecipe
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
								expand = { isStatInfoExpanded1.value = !isStatInfoExpanded1.value },
								isExpanded = isStatInfoExpanded1.value
							)
							AnimatedVisibility(isStatInfoExpanded1.value) {
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
								expand = { isStatInfoExpanded2.value = !isStatInfoExpanded2.value },
								isExpanded = isStatInfoExpanded2.value
							)
							AnimatedVisibility(isStatInfoExpanded2.value) {
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
								expand = { isStatInfoExpanded3.value = !isStatInfoExpanded3.value },
								isExpanded = isStatInfoExpanded3.value
							)
							AnimatedVisibility(isStatInfoExpanded3.value) {
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
								expand = { isStatInfoExpanded4.value = !isStatInfoExpanded4.value },
								isExpanded = isStatInfoExpanded4.value
							)
							AnimatedVisibility(isStatInfoExpanded4.value) {
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
								expand = { isStatInfoExpanded5.value = !isStatInfoExpanded5.value },
								isExpanded = isStatInfoExpanded5.value
							)
							AnimatedVisibility(isStatInfoExpanded5.value) {
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
								expand = { isStatInfoExpanded6.value = !isStatInfoExpanded6.value },
								isExpanded = isStatInfoExpanded6.value
							)
							AnimatedVisibility(isStatInfoExpanded6.value) {
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
								expand = { isStatInfoExpanded7.value = !isStatInfoExpanded7.value },
								isExpanded = isStatInfoExpanded7.value
							)
							AnimatedVisibility(isStatInfoExpanded7.value) {
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
								expand = { isStatInfoExpanded8.value = !isStatInfoExpanded8.value },
								isExpanded = isStatInfoExpanded8.value
							)
							AnimatedVisibility(isStatInfoExpanded8.value) {
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
							itemData = craftingStation,
							subTitle = if (category == FoodSubCategory.UNCOOKED_FOOD) "Cook at Station to Consume" else "Requires Cooking Station",
							contentScale = ContentScale.Fit,
							painter = painterResource(R.drawable.food_bg)
						)
					}

					if (recipeItems.isNotEmpty()) {
						SlavicDivider()
						DroppedItemsSection(
							icon = Lucide.CookingPot,
							list = recipeItems,
							starLevel = 0,
							title = "Recipe",
							subTitle = "Ingredients required to craft this item",
						)
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
			uiState = FoodDetailUiState(
				food = fakeFood,
				craftingCookingStation = craftingStation,
				foodForRecipe = fakeFoodList,
				materialsForRecipe = fakeMaterialsList,
				isLoading = false,
				error = null
			),
			onBack = {},
			category = FoodSubCategory.COOKED_FOOD,
		)
	}

}
