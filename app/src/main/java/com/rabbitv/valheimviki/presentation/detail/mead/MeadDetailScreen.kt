package com.rabbitv.valheimviki.presentation.detail.mead


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCardPainter
import com.rabbitv.valheimviki.presentation.components.images.SmallFramedImage
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.detail.food.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.detail.food.DarkGlassStatCardPainter
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiState
import com.rabbitv.valheimviki.presentation.detail.mead.model.RecipeMeadData
import com.rabbitv.valheimviki.presentation.detail.mead.viewmodel.MeadDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MeadDetailScreen(
	onBack: () -> Unit,
	category: FoodSubCategory,
	viewModel: MeadDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	MeadDetailContent(
		uiState = uiState,
		onBack = onBack,
		category = category
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MeadDetailContent(
	uiState: MeadDetailUiState,
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
	val backButtonVisibleState = remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()
	var previousScrollValue by remember { mutableIntStateOf(0) }

	LaunchedEffect(scrollState.value) {
		val currentScroll = scrollState.value

		when {
			currentScroll == 0 -> backButtonVisibleState.value = true
			currentScroll < previousScrollValue -> backButtonVisibleState.value = true
			currentScroll > previousScrollValue -> backButtonVisibleState.value = false
		}

		previousScrollValue = currentScroll
	}
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
	Image(
		painter = painterResource(R.drawable.main_background),
		contentDescription = "bg",
		contentScale = ContentScale.FillBounds,
		modifier = Modifier.fillMaxSize()
	)

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

			uiState.mead?.let { mead ->
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
					SmallFramedImage(mead.imageUrl)
					Text(
						mead.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					DetailExpandableText(
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						text = mead.description,
						isExpanded = isExpandable
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = BODY_CONTENT_PADDING.dp),
						verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
					) {

						SlavicDivider()
						if (shouldShowValue(mead.health)) {
							DarkGlassStatCard(
								Lucide.Heart,
								"Health",
								mead.health.toString(),
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
						if (shouldShowValue(mead.healing)) {
							DarkGlassStatCardPainter(
								healingPainter,
								"Healing",
								mead.healing.toString(),
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
						if (shouldShowValue(mead.stamina)) {
							DarkGlassStatCardPainter(
								staminaPainter,
								"Stamina",
								mead.stamina.toString(),
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
						if (shouldShowValue(mead.duration)) {
							DarkGlassStatCard(
								Lucide.Clock2,
								"Duration",
								"${mead.duration.toString()} min",
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
						if (shouldShowValue(mead.eitr)) {
							DarkGlassStatCard(
								Lucide.Wand,
								"Eitr",
								mead.eitr.toString(),
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
						if (shouldShowValue(mead.weight)) {
							DarkGlassStatCard(
								Lucide.Weight,
								"Weight",
								mead.weight.toString(),
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
								Lucide.Info,
								"Fork Type",
								mead.forkType.toString(),
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
						if (shouldShowValue(mead.stackSize)) {
							DarkGlassStatCard(
								Lucide.Layers2,
								"Stack Size",
								mead.stackSize.toString(),
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
							contentScale = ContentScale.FillBounds,
							painter = painterResource(R.drawable.food_bg)
						)
					}

					if (recipeItems.isNotEmpty()) {
						SlavicDivider()
						DroppedItemsSection(
							icon = Lucide.CookingPot,
							list = recipeItems,
							starLevel = 0,
							title = "RECIPE",
							subTitle = "Ingredients required to craft this item",
						)
					}
				}
			}
			AnimatedVisibility(
				visible = backButtonVisibleState.value,
				enter = fadeIn(),
				exit = fadeOut(),
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp)
			) {
				FilledIconButton(
					onClick = onBack,
					shape = RoundedCornerShape(12.dp),
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = ForestGreen10Dark,
					),
					modifier = Modifier.size(56.dp)
				) {
					Icon(
						Icons.AutoMirrored.Rounded.ArrowBack,
						contentDescription = "Back",
						tint = Color.White
					)
				}
			}
		}
	}
}





@RequiresApi(Build.VERSION_CODES.S)
@Preview("MeadDetailContentPreview", showBackground = true)
@Composable
fun PreviewMeadDetailContentCooked() {
	val exampleMead = Mead(
		id = "mead_tasty",
		imageUrl = "https://example.com/images/mead_tasty.png",
		category = "Consumable",
		subCategory = "Mead",
		name = "Tasty Mead",
		description = "Increases stamina regeneration but reduces health regeneration.",
		recipeOutput = "TastyMead",
		effect = "Stamina Regen +300%, Health Regen -50%",
		duration = "10m",
		cooldown = "2m",
		order = 1
	)

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

	val fakeRecipeMead = listOf(
		RecipeMeadData(
			itemDrop = exampleMead,
			quantityList = listOf(1, 2, 3),
			chanceStarList = listOf(100, 75, 50)
		),
		RecipeMeadData(
			itemDrop = exampleMead,
			quantityList = listOf(1, 2, 3),
			chanceStarList = listOf(100, 75, 50)
		)
	)
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
		MeadDetailContent(
			uiState = MeadDetailUiState(
				mead = exampleMead,
				craftingCookingStation = craftingStation,
				foodForRecipe = fakeFoodList,
				meadForRecipe = fakeRecipeMead,
				materialsForRecipe = fakeMaterialsList,
				isLoading = false,
				error = null
			),
			onBack = {},
			category = FoodSubCategory.COOKED_FOOD,
		)
	}

}
