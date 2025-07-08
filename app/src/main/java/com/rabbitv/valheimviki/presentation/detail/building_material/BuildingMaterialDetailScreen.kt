package com.rabbitv.valheimviki.presentation.detail.building_material


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Gauge
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.ConsumableDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid.TwoColumnGrid
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.CustomItemCard
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel.BuildingMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BuildingMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: BuildingMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	BuildingMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BuildingMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	uiState: BuildingMaterialUiState,

	) {

	val scrollState = rememberScrollState()
	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isExpandable = remember { mutableStateOf(false) }
	painterResource(R.drawable.main_background)

	val comfortDescription =
		"<p><b>Comfort</b> level determines the duration of <a href=\"/wiki/Resting_Effect\" class=\"mw-redirect\" title=\"Resting Effect\">Resting Effect</a>. Base duration is 8 minutes, with each comfort level stacking 1 minute up to 24 minutes with usual items and 26 minutes with rare seasonal items.  \n" +
				"</p><p>The max comfort reachable normally is 17. If near a <a href=\"/wiki/Maypole\" title=\"Maypole\">Maypole</a> 18 or with the seasonal <a href=\"/wiki/Yule_tree\" title=\"Yule tree\">Yule tree</a> 19.  \n" +
				"</p><p>When entering most dungeons (such as <a href=\"/wiki/Burial_Chambers\" title=\"Burial Chambers\">Burial Chambers</a>) a fire can be constructed to easily gain Comfort 3 (and a 10-minute <a href=\"/wiki/Rested_Effect\" class=\"mw-redirect\" title=\"Rested Effect\">Rested Effect</a>).  \n" +
				"</p><p>Note that while you can add more than one furniture item from the categories below, only the one with the highest comfort will influence the comfort rating. \n</p>"

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
			uiState.buildingMaterial?.let { buildingMaterial ->
				Column(
					modifier = Modifier
						.verticalScroll(scrollState)
						.padding(
							top = 20.dp,
							bottom = 70.dp
						),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,

					) {
					FramedImage(buildingMaterial.imageUrl)
					Text(
						buildingMaterial.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)


					if (!buildingMaterial.description.isBlank()) {
						SlavicDivider()
						DetailExpandableText(
							text = buildingMaterial.description,
							boxPadding = BODY_CONTENT_PADDING.dp,
							isExpanded = isExpandable
						)

					}
					if (uiState.buildingMaterial.comfortLevel != null) {
						SlavicDivider()
						DarkGlassStatCard(
							icon = Lucide.Gauge,
							label = "Comfort Level",
							value = uiState.buildingMaterial.comfortLevel.toString(),
							expand = { isStatInfoExpanded1.value = !isStatInfoExpanded1.value },
							isExpanded = isStatInfoExpanded1.value,
						)
						AnimatedVisibility(
							visible = isStatInfoExpanded1.value,
							enter = expandVertically() + fadeIn(),
							exit = shrinkVertically() + fadeOut()
						) {
							Text(
								text = AnnotatedString.fromHtml(comfortDescription),
								modifier = Modifier
									.padding(BODY_CONTENT_PADDING.dp)
									.fillMaxWidth(),
								style = MaterialTheme.typography.bodyLarge
							)
						}
					}
					if (uiState.craftingStation.isNotEmpty()) {
						SlavicDivider()
						uiState.craftingStation.forEach { craftingStation ->
							CardImageWithTopLabel(
								onClickedItem = {
									val destination =
										BuildingDetailDestination.CraftingObjectDetail(
											craftingObjectId = craftingStation.id
										)
									onItemClick(destination)
								},
								itemData = craftingStation,
								subTitle = "Crafting station that must be near the construction",
								contentScale = ContentScale.Fit,

								)
							Spacer(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp))
						}
					}
					if (uiState.materials.isNotEmpty() || uiState.foods.isNotEmpty()) {
						SlavicDivider()
						Text(
							"Required Materials",
							style = MaterialTheme.typography.headlineMedium,
							modifier = Modifier.padding(
								top = BODY_CONTENT_PADDING.dp,
								start = BODY_CONTENT_PADDING.dp,
								end = BODY_CONTENT_PADDING.dp
							)
						)
						HorizontalDivider(
							modifier = Modifier
								.fillMaxWidth(0.8f)
								.padding(BODY_CONTENT_PADDING.dp),
							thickness = 1.dp,
							color = Color.White
						)
						TwoColumnGrid {
							for (material in uiState.materials) {
								CustomItemCard(
									onItemClick = {
										material.itemDrop.subCategory?.let { subCategory ->
											val destination =
												NavigationHelper.routeToMaterial(
													subCategory,
													material.itemDrop.id
												)
											onItemClick(destination)
										}
									},
									fillWidth = 0.45f,
									imageUrl = material.itemDrop.imageUrl,
									name = material.itemDrop.name,
									quantity = material.quantityList.firstOrNull()
								)
							}
							for (food in uiState.foods) {
								CustomItemCard(
									onItemClick = {
										food.itemDrop.subCategory?.let {
											val subCategory =
												NavigationHelper.stringToFoodSubCategory(it)
											val destination =
												ConsumableDetailDestination.FoodDetail(
													food.itemDrop.id,
													subCategory
												)
											onItemClick(destination)
										}
									},
									fillWidth = 0.45f,
									imageUrl = food.itemDrop.imageUrl,
									name = food.itemDrop.name,
									quantity = food.quantityList.firstOrNull()
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
			}
		}
	}
}


//@RequiresApi(Build.VERSION_CODES.S)
//@Preview("ToolDetailContentPreview", showBackground = true)
//@Composable
//fun PreviewToolDetailContentCooked() {
//
//
//	ValheimVikiAppTheme {
//		GeneralMaterialDetailContent(
//			uiState = GeneralMaterialUiState(
//				material = FakeData.generateFakeMaterials()[0],
//				isLoading = false,
//				error = null
//			),
//			onBack = {},
//		)
//	}
//
//}
