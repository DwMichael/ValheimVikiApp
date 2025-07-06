package com.rabbitv.valheimviki.presentation.detail.mead


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
import androidx.compose.runtime.getValue
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
import com.composables.icons.lucide.ClockArrowDown
import com.composables.icons.lucide.CookingPot
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Layers2
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
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
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData
import com.rabbitv.valheimviki.presentation.detail.mead.model.MeadDetailUiState
import com.rabbitv.valheimviki.presentation.detail.mead.model.RecipeMeadData
import com.rabbitv.valheimviki.presentation.detail.mead.viewmodel.MeadDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MeadDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	category: MeadSubCategory,
	viewModel: MeadDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	MeadDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
		category = category
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MeadDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	uiState: MeadDetailUiState,

	category: MeadSubCategory
) {
	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isStatInfoExpanded2 = remember { mutableStateOf(false) }
	val isStatInfoExpanded3 = remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()
	val craftingStationPainter = painterResource(R.drawable.food_bg)


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

	val mead = uiState.mead
	val showStatsSection =
		mead != null && (shouldShowValue(mead.duration) || shouldShowValue(mead.cooldown) || shouldShowValue(
			mead.recipeOutput
		))
	val showCraftingStationSection = uiState.craftingCookingStation != null


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
					FramedImage(mead.imageUrl)
					Text(
						mead.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					if (mead.description != null) {
						Box(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)) {
							DetailExpandableText(
								text = mead.description,
								isExpanded = isExpandable
							)
						}
					}
					if (uiState.foodForRecipe.isNotEmpty() || uiState.materialsForRecipe.isNotEmpty() || uiState.meadForRecipe.isNotEmpty()) {
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
									icon = if (category == MeadSubCategory.MEAD_BASE) Lucide.CookingPot else Lucide.FlaskRound,
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
											NavigationHelper.routeToMaterial(
												item.itemDrop.subCategory,
												item.itemDrop.id
											)
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
										val subCategory =
											NavigationHelper.stringToFoodSubCategory(item.itemDrop.subCategory)
										val destination = ConsumableDetailDestination.FoodDetail(
											item.itemDrop.id,
											subCategory
										)
										onItemClick(destination)
									},
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = item.itemDrop.imageUrl,
									name = item.itemDrop.name,
									quantity = item.quantityList.firstOrNull()
								)
							}
							for (item in uiState.meadForRecipe) {
								CustomItemCard(
									onItemClick = {
										val subCategory =
											NavigationHelper.stringToMeadSubCategory(item.itemDrop.subCategory)
										val destination = ConsumableDetailDestination.MeadDetail(
											item.itemDrop.id,
											subCategory
										)
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
					if (showStatsSection) {
						TridentsDividedRow("Stats")
						Column(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = BODY_CONTENT_PADDING.dp),
							verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
						) {
							if (shouldShowValue(mead.duration)) {
								DarkGlassStatCard(
									icon = Lucide.Clock2,
									label = "Duration",
									value = "${mead.duration.toString()} min",
									expand = {
										isStatInfoExpanded2.value = !isStatInfoExpanded2.value
									},
									isExpanded = isStatInfoExpanded2.value
								)
								AnimatedVisibility(isStatInfoExpanded2.value) {
									Text(
										text = "How long this potion's effects remain active after consumption. The timer begins immediately upon eating and cannot be paused or extended.",
										modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
										style = MaterialTheme.typography.bodyLarge
									)
								}
							}
							if (shouldShowValue(mead.cooldown)) {
								DarkGlassStatCard(
									icon = Lucide.ClockArrowDown,
									label = "Cooldown",
									value = mead.cooldown.toString(),
									expand = {
										isStatInfoExpanded3.value = !isStatInfoExpanded3.value
									},
									isExpanded = isStatInfoExpanded3.value
								)
								AnimatedVisibility(isStatInfoExpanded3.value) {
									Text(
										text = "The cooldown is the time you must wait before consuming another potion or mead of the same type. It prevents immediate re-use and encourages strategic planning in combat or exploration.",
										modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
										style = MaterialTheme.typography.bodyLarge
									)
								}
							}
							if (shouldShowValue(mead.recipeOutput)) {
								DarkGlassStatCard(
									icon = Lucide.Layers2,
									label = "Stack size",
									value = mead.recipeOutput.toString(),
									expand = {
										isStatInfoExpanded1.value = !isStatInfoExpanded1.value
									},
									isExpanded = isStatInfoExpanded1.value
								)
								AnimatedVisibility(isStatInfoExpanded1.value) {
									Text(
										text = "The amount of meads produced by fermenting the mead base for two in-game days.",
										modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
										style = MaterialTheme.typography.bodyLarge
									)
								}
							}
						}
					}

					if (showCraftingStationSection) {
						if (showStatsSection || (uiState.foodForRecipe.isNotEmpty() || uiState.materialsForRecipe.isNotEmpty() || uiState.meadForRecipe.isNotEmpty())) {
							SlavicDivider()
						}
						CardImageWithTopLabel(
							onClickedItem ={
								val destination = BuildingDetailDestination.CraftingObjectDetail(
									craftingObjectId = uiState.craftingCookingStation.id
								)
								onItemClick(destination)
							},
							itemData = uiState.craftingCookingStation,
							subTitle = if (category == MeadSubCategory.MEAD_BASE) "Requires cooking station" else "Requires fermenting station",
							contentScale = ContentScale.Fit,
							painter = craftingStationPainter
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
		recipeOutput = "6",
		effect = "Stamina Regen +300%, Health Regen -50%",
		duration = "10:00",
		cooldown = "02:00",
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
			onItemClick = {},
			category = MeadSubCategory.MEAD_BASE,
		)
	}

}
