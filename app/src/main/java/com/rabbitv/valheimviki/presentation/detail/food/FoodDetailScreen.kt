package com.rabbitv.valheimviki.presentation.detail.food

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.animated_stat_card.AnimatedStatCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedGrid
import com.rabbitv.valheimviki.presentation.components.grid.nested.NestedItems
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeaderData
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.food.viewmodel.FoodDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import com.rabbitv.valheimviki.utils.shouldShowValue

@Composable
fun FoodDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	category: FoodSubCategory,
	viewModel: FoodDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { viewModel.uiEvent(FoodDetailUiEvent.ToggleFavorite) }

	FoodDetailContent(
		uiState = uiState,
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		category = category
	)

}


@Composable
fun FoodDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: FoodDetailUiState,
	category: FoodSubCategory
) {
	val healingPainter = painterResource(R.drawable.heart_plus__2_)
	val staminaPainter = painterResource(R.drawable.runing)
	val scrollState = rememberScrollState()


	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}

	val showRecipeSection = remember(uiState.materialsForRecipe, uiState.foodForRecipe) {
		(uiState.materialsForRecipe as? UIState.Success)?.data?.isNotEmpty() == true ||
				(uiState.foodForRecipe as? UIState.Success)?.data?.isNotEmpty() == true
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
							bottom = 80.dp
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
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = BODY_CONTENT_PADDING.dp),
						verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
					) {
						if (shouldShowValue(food.health)) {
							SlavicDivider()
							AnimatedStatCard(
								id = "heart_stat",
								icon = Lucide.Heart,
								label = stringResource(R.string.health),
								value = "${food.health.toString()} min",
								details = "The amount of health points this food adds to your health bar. Health regenerates at 1% per second when above 25% HP. Maximum health is crucial for effective shield use as it directly affects your stagger resistance capacity.",
							)
						}
						if (shouldShowValue(food.healing)) {
							AnimatedStatCard(
								id = "healing_stat",
								painter = healingPainter,
								label = stringResource(R.string.healing),
								value = "${food.healing.toString()}/s",
								details = "The rate of health regeneration in HP per second while this food effect is active. This healing occurs continuously throughout the food's duration.",
							)
						}
						if (shouldShowValue(food.stamina)) {
							AnimatedStatCard(
								id = "stamina_stat",
								painter = staminaPainter,
								label = stringResource(R.string.stamina),
								value = food.stamina.toString(),
								details = "The amount of stamina points this food adds to your stamina bar. Stamina is used for running, jumping, attacking, and blocking. Higher stamina allows for longer combat engagements and exploration.",
							)
						}
						if (shouldShowValue(food.duration)) {
							AnimatedStatCard(
								id = "duration_stat",
								icon = Lucide.Clock2,
								label = stringResource(R.string.duration),
								value = "${food.duration.toString()} min",
								details = "How long this food's effects remain active after consumption. The timer begins immediately upon eating and cannot be paused or extended.",
							)
						}
						if (shouldShowValue(food.eitr)) {
							AnimatedStatCard(
								id = "eitr_stat",
								icon = Lucide.Wand,
								label = stringResource(R.string.eitr),
								value = food.eitr.toString(),
								details = "The amount of eitr (magic energy) this food provides. Eitr is required for casting magic spells and using staffs. Only certain foods provide this mystical resource.",
							)
						}
						if (shouldShowValue(food.weight)) {
							AnimatedStatCard(
								id = "weight_stat",
								icon = Lucide.Weight,
								label = stringResource(R.string.weight),
								value = food.weight.toString(),
								details = "The weight of one unit of this food in your inventory. Total weight affects movement speed when overencumbered.",
							)
						}
						if (shouldShowValue(food.forkType)) {
							AnimatedStatCard(
								id = "info_stat",
								icon = Lucide.Info,
								label = stringResource(R.string.fork_type),
								value = food.forkType.toString(),
								details = "The fork icon color indicates this food's primary benefit: Red fork for health-focused foods, yellow fork for stamina-focused foods, blue fork for eitr-focused foods, and white fork for balanced foods that provide equal benefits to multiple stats.",
							)
						}
						if (shouldShowValue(food.stackSize)) {
							AnimatedStatCard(
								id = "stackSize_stat",
								icon = Lucide.Layers2,
								label = stringResource(R.string.stack_size),
								value = food.stackSize.toString(),
								details = "The maximum number of this food item that can be stored in a single inventory slot. Higher stack sizes save valuable inventory space during long expeditions.",
							)
						}

					}
					uiState.craftingCookingStation?.let { craftingStation ->
						SlavicDivider()
						CardImageWithTopLabel(
							onClickedItem = {
								val destination = BuildingDetailDestination.CraftingObjectDetail(
									craftingObjectId = craftingStation.id
								)
								onItemClick(destination)
							},
							itemData = craftingStation,
							subTitle = if (category == FoodSubCategory.COOKED_FOOD) "Cook at Station to Consume" else "Requires Cooking Station To Make",
							contentScale = ContentScale.Fit,
							painter = painterResource(R.drawable.food_bg)
						)
					}
					if (showRecipeSection) {
						TridentsDividedRow()
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp)
						) {
							SectionHeader(
								data = SectionHeaderData(
									title = "Recipe",
									subTitle = "Ingredients required to craft this item",
									icon = Lucide.CookingPot
								)
							)
						}

						Spacer(modifier = Modifier.padding(6.dp))

					}

					UiSection(
						state = uiState.materialsForRecipe,
						divider = {}
					) { data ->
						NestedGrid(
							nestedItems = NestedItems(items = data),
							gridCells = 2
						) { item ->
							CustomItemCard(
								itemData = item.itemDrop,
								onItemClick = handleClick,
								fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
								imageUrl = item.itemDrop.imageUrl,
								name = item.itemDrop.name,
								quantity = item.quantityList.firstOrNull()
							)
						}
					}
					UiSection(
						state = uiState.foodForRecipe,
						divider = {}
					) { data ->
						NestedGrid(
							nestedItems = NestedItems(items = data),
							gridCells = 2
						) { item ->
							CustomItemCard(
								itemData = item.itemDrop,
								onItemClick = handleClick,
								fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
								imageUrl = item.itemDrop.imageUrl,
								name = item.itemDrop.name,
								quantity = item.quantityList.firstOrNull()
							)
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
			uiState.food?.let {
				FavoriteButton(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(16.dp),
					isFavorite = uiState.isFavorite,
					onToggleFavorite = { onToggleFavorite() }
				)
			}
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
			onToggleFavorite = { },
			uiState = FoodDetailUiState(
				food = fakeFood,
				craftingCookingStation = craftingStation,
				foodForRecipe = UIState.Success(fakeFoodList),
				materialsForRecipe = UIState.Success(fakeMaterialsList)
			),
			category = FoodSubCategory.COOKED_FOOD,
		)
	}

}
