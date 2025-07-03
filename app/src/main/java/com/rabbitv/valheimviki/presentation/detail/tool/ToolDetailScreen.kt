package com.rabbitv.valheimviki.presentation.detail.tool


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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pickaxe
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.section_header.SectionHeader
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.tool.model.ToolDetailUiState
import com.rabbitv.valheimviki.presentation.detail.tool.viewmodel.ToolDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.CUSTOM_ITEM_CARD_FILL_WIDTH
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

	val oreDepositData = HorizontalPagerData(
		title = "Ore Deposits",
		subTitle = "Ore Deposits that can be mine with this pickaxe",
		icon = Lucide.Pickaxe,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)
	val scrollState = rememberScrollState()
	remember { mutableIntStateOf(0) }
	val craftingStationPainter = painterResource(R.drawable.food_bg)

	val isExpandable = remember { mutableStateOf(false) }



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
						TwoColumnGrid {
							for (item in uiState.relatedMaterials) {
								CustomItemCard(
									fillWidth = CUSTOM_ITEM_CARD_FILL_WIDTH,
									imageUrl = item.material.imageUrl,
									name = item.material.name,
									quantity = item.quantityList.firstOrNull()
								)
							}
						}
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
					if (!uiState.tool.howToUse.isNullOrEmpty() && uiState.tool.howToUse != "null") {
						TridentsDividedRow()
						Text(
							modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
							text = AnnotatedString.fromHtml(
								uiState.tool.howToUse,
							),
							style = MaterialTheme.typography.bodyLarge
						)
					}
					if (!uiState.tool.generalInfo.isNullOrEmpty() && uiState.tool.generalInfo != "null") {
						TridentsDividedRow()
						Text(
							modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
							text = AnnotatedString.fromHtml(
								uiState.tool.generalInfo,
							),
							style = MaterialTheme.typography.bodyLarge
						)
					}
					if (uiState.relatedOreDeposits.isNotEmpty()) {
						SlavicDivider()
						HorizontalPagerSection(
							list = uiState.relatedOreDeposits,
							data = oreDepositData,
						)
					}
					if (uiState.relatedNpc != null) {
						SlavicDivider()
						CardImageWithTopLabel(
							itemData = uiState.relatedNpc,
							subTitle = "Npc that sell this item",
							contentScale = ContentScale.Crop,
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
