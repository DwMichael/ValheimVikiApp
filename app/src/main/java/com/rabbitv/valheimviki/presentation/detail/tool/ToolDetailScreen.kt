package com.rabbitv.valheimviki.presentation.detail.tool


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.grid.custom_column_grid.CustomColumnGrid
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.tool.model.ToolDetailUiState
import com.rabbitv.valheimviki.presentation.detail.tool.viewmodel.ToolDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import com.rabbitv.valheimviki.utils.mapUpgradeToolsInfoToGridList

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ToolDetailScreen(
	onBack: () -> Unit,
	viewModel: ToolDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	ToolDetailContent(
		uiState = uiState,
		onBack = onBack,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ToolDetailContent(
	uiState: ToolDetailUiState,
	onBack: () -> Unit,
) {

	val scrollState = rememberScrollState()
	val previousScrollValue = remember { mutableIntStateOf(0) }
	val craftingStationPainter = painterResource(R.drawable.food_bg)
	val backButtonVisibleState by remember {
		derivedStateOf {
			val currentScroll = scrollState.value
			val previous = previousScrollValue.intValue
			val isVisible = when {
				currentScroll == 0 -> true
				currentScroll < previous -> true
				currentScroll > previous -> false
				else -> true
			}
			previousScrollValue.intValue = currentScroll
			isVisible
		}
	}

	val isExpandable = remember { mutableStateOf(false) }


	val painterBackgroundImage = painterResource(R.drawable.main_background)

	Image(
		painter = painterBackgroundImage,
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
			uiState.tool?.let { tool ->
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
					FramedImage(tool.imageUrl)
					Text(
						tool.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()

					DetailExpandableText(
						text = tool.description,
						boxPadding = BODY_CONTENT_PADDING.dp,
						isExpanded = isExpandable
					)
					SlavicDivider()

					if (uiState.relatedMaterials.isNotEmpty() && uiState.tool.upgradeInfoList.isNullOrEmpty()) {

						Box(
							modifier = Modifier
								.fillMaxWidth()
								.wrapContentHeight()
								.padding(horizontal = BODY_CONTENT_PADDING.dp)
						) {
							SectionHeader(
								data = HorizontalPagerWithHeaderData(
									title = "Crafting Ingredients",
									subTitle = "Items required to craft this item",
									icon = Lucide.Hammer,
									iconRotationDegrees = 0f,
									contentScale = ContentScale.Crop,
									starLevelIndex = 0,
								),
								modifier = Modifier,
							)
						}

						Spacer(modifier = Modifier.padding(6.dp))
						CustomColumnGrid(
							list = uiState.relatedMaterials,
							getImageUrl = { it.material.imageUrl },
							getName = { it.material.name },
							getQuantity = { it.quantityList[0] }
						)
					}
					if (!tool.upgradeInfoList.isNullOrEmpty()) {
						Text(
							"Upgrade Information",
							modifier = Modifier.padding(
								start = BODY_CONTENT_PADDING.dp,
								end = BODY_CONTENT_PADDING.dp,
								bottom = BODY_CONTENT_PADDING.dp
							),
							color = PrimaryWhite,
							style = MaterialTheme.typography.headlineMedium
						)

						tool.upgradeInfoList.forEachIndexed { levelIndex, upgradeInfoForLevel ->
							val upgradeStats = mapUpgradeToolsInfoToGridList(upgradeInfoForLevel)
							LevelInfoCard(
								modifier = Modifier.padding(
									horizontal = BODY_CONTENT_PADDING.dp,
									vertical = 8.dp
								),
								level = levelIndex,
								upgradeStats = upgradeStats,
								materialsForUpgrade = uiState.relatedMaterials,
							)
						}

					}

					if (uiState.relatedCraftingStation != null) {
						if (uiState.relatedMaterials.isNotEmpty() || !tool.upgradeInfoList.isNullOrEmpty()) {
							SlavicDivider()
						}
						CardImageWithTopLabel(
							itemData = uiState.relatedCraftingStation,
							subTitle = "Requires crafting station",
							contentScale = ContentScale.Fit,
							painter = craftingStationPainter
						)
					}
				}
			}
			AnimatedVisibility(
				visible = backButtonVisibleState,
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
@Preview("ToolDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {
	val exampleTool = ItemTool(
		id = "Tool_tasty",
		imageUrl = "https://example.com/images/Tool_tasty.png",
		category = "Consumable",
		subCategory = "Tool",
		name = "Tasty Tool",
		description = "Increases stamina regeneration but reduces health regeneration.",
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


	val fakeMaterialsList = listOf(
		MaterialUpgrade(
			material = fakeMaterial,
			quantityList = listOf(3, 4, 5)
		),
		MaterialUpgrade(
			material = fakeMaterial,
			quantityList = listOf(2, 3, 4)
		),
		MaterialUpgrade(
			material = fakeMaterial,
			quantityList = listOf(1, 2, 3)
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
		ToolDetailContent(
			uiState = ToolDetailUiState(

				isLoading = false,
				error = null,
				tool = exampleTool,
				relatedCraftingStation = craftingStation,
				relatedMaterials = fakeMaterialsList
			),
			onBack = {},
		)
	}

}
