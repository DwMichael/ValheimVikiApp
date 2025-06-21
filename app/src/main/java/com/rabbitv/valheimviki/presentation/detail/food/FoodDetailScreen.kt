package com.rabbitv.valheimviki.presentation.detail.food

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Clock2
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Layers2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Utensils
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.Weight
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.viewmodel.FoodDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.BlackBrownBorder
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


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

			uiState.food?.let { food ->
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(scrollState)
						.padding(
							start = BODY_CONTENT_PADDING.dp,
							end = BODY_CONTENT_PADDING.dp,
							top = 20.dp,
							bottom = 45.dp
						),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,

					) {
					FoodImage(food.imageUrl)
					Text(
						food.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					DetailExpandableText(
						text = food.description,
						isExpanded = isExpandable
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = BODY_CONTENT_PADDING.dp),
						verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
					) {

						uiState.craftingCookingStation?.let { craftingStation ->
							TridentsDividedRow()
							CardImageWithTopLabel(
								itemData = craftingStation,
								subTitle = if (category == FoodSubCategory.UNCOOKED_FOOD) "Cook at Station to Consume" else "Requires Cooking Station",
								contentScale = ContentScale.FillBounds,
							)
						}
						SlavicDivider()
						if (shouldShowValue(food.health)) {
							DarkGlassStatCard(
								Lucide.Heart,
								"Health",
								food.health.toString(),
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
								Lucide.Clock2,
								"Duration",
								"${food.duration.toString()} min",
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
								Lucide.Wand,
								"Eitr",
								food.eitr.toString(),
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
								Lucide.Weight,
								"Weight",
								food.weight.toString(),
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
								food.forkType.toString(),
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
								Lucide.Layers2,
								"Stack Size",
								food.stackSize.toString(),
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

@Composable
fun DarkGlassStatCard(
	icon: ImageVector,
	label: String,
	value: String,
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	isExpanded: Boolean,
) {
	BaseDarkGlassStatCard(
		iconContent = {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = Color(0xFFFF6B35),
				modifier = Modifier.size(24.dp)
			)
		},
		label = label,
		value = value,
		modifier = modifier.clickable { expand() },
		expand = expand,
		isExpanded = isExpanded
	)
}

@Composable
fun DarkGlassStatCardPainter(
	painter: Painter,
	label: String,
	value: String,
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	isExpanded: Boolean,
) {
	BaseDarkGlassStatCard(
		iconContent = {
			Icon(
				painter = painter,
				contentDescription = null,
				tint = Color(0xFFFF6B35),
				modifier = Modifier.size(24.dp)
			)
		},
		label = label,
		value = value,
		modifier = modifier.clickable { expand() },
		expand = expand,
		isExpanded = isExpanded
	)
}

@Composable
private fun BaseDarkGlassStatCard(
	iconContent: @Composable () -> Unit,
	label: String,
	value: String,
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	isExpanded: Boolean
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(60.dp)
			.clip(Shapes.large)
			.background(
				Color.Black.copy(alpha = 0.3f)
			)
			.border(
				width = 1.dp,
				color = Color(0xFF4A4A4A).copy(alpha = 0.5f),
				shape = Shapes.large
			)
	) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			Box(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						renderEffect = RenderEffect.createBlurEffect(
							10f,
							10f,
							Shader.TileMode.CLAMP
						).asComposeRenderEffect()
					}
			)
		}

		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 24.dp),
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			iconContent()

			Spacer(modifier = Modifier.width(16.dp))

			Text(
				text = label,
				color = Color(0xFFFF6B35),
				style = MaterialTheme.typography.bodyLarge.copy(
					fontSize = 22.sp,
					fontWeight = FontWeight.Bold
				),
				letterSpacing = 1.sp
			)

			Spacer(modifier = Modifier.width(10.dp))


			Text(
				modifier = Modifier
					.weight(1f)
					.padding(horizontal = 5.dp),
				text = value,
				color = Color.White,
				style = MaterialTheme.typography.bodyLarge.copy(
					fontWeight = FontWeight.Bold,
					lineHeight = 18.sp
				),
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.End
			)

			IconButton(
				onClick = expand,
			) {
				Icon(
					imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
					contentDescription = null,
					tint = Color(0xFFFF6B35),
					modifier = Modifier.size(24.dp)
				)
			}
		}
	}
}


@Composable
fun FoodImage(imageUrl: String) {
	Box(
		Modifier
			.size(150.dp)
			.border(1.dp, BlackBrownBorder, Shapes.large)
	)
	{
		Image(
			painter = painterResource(R.drawable.bg_food),
			contentDescription = "bg",
			contentScale = ContentScale.FillBounds,
			modifier = Modifier
				.clip(Shapes.large)
				.fillMaxSize()
		)
		AsyncImage(
			modifier = Modifier
				.fillMaxSize(0.7f)
				.align(Alignment.Center),
			model = ImageRequest.Builder(LocalContext.current)
				.data(imageUrl)
				.crossfade(true)
				.build(),
			contentDescription = "Food Image",
			error = if (LocalInspectionMode.current) painterResource(R.drawable.testweapon) else null,
			contentScale = ContentScale.FillBounds,
		)

	}
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview("FoodStatCard")
@Composable
fun PreviewFoodStatCard() {

	ValheimVikiAppTheme {
		DarkGlassStatCard(Lucide.Utensils, "Health", "100", expand = {}, isExpanded = false)
	}

}

@Preview("FoodImage")
@Composable
fun PreviewFoodImage() {
	ValheimVikiAppTheme {
		FoodImage("")
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
				isLoading = false,
				error = null
			),
			onBack = {},
			category = FoodSubCategory.COOKED_FOOD,
		)
	}

}
