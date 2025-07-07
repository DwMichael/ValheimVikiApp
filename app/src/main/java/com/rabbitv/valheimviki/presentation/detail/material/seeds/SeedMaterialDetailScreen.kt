package com.rabbitv.valheimviki.presentation.detail.material.seeds


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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Gauge
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.EquipmentDetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.material.seeds.model.SeedUiState
import com.rabbitv.valheimviki.presentation.detail.material.seeds.viewmodel.SeedMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SeedMaterialDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: SeedMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	SeedMaterialDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SeedMaterialDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	uiState: SeedUiState,
) {

	val scrollState = rememberScrollState()
	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isStatInfoExpanded2 = remember { mutableStateOf(false) }
	val isExpandable = remember { mutableStateOf(false) }

	val toolsData = HorizontalPagerData(
		title = "Tools",
		subTitle = "Tools needed to plant this seed",
		icon = Lucide.Gavel,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val treesData = HorizontalPagerData(
		title = "Trees",
		subTitle = "Trees from witch this seed drop",
		icon = Lucide.Trees,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this seed",
		icon = Lucide.MapPinned,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)


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
			uiState.material?.let { material ->
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
					FramedImage(material.imageUrl)
					Text(
						material.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()

					material.description?.let {
						DetailExpandableText(
							text = material.description,
							boxPadding = BODY_CONTENT_PADDING.dp,
							isExpanded = isExpandable
						)

					}
					if (uiState.biomes.isNotEmpty()) {
						SlavicDivider()
						Text(
							modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
							text = "PRIMARY SPAWNS",
							textAlign = TextAlign.Left,
							style = MaterialTheme.typography.titleSmall,
							maxLines = 1,
							overflow = TextOverflow.Visible
						)
						uiState.biomes.forEach { biome ->
							CardWithOverlayLabel(
								onClickedItem = {
									val destination =
										WorldDetailDestination.BiomeDetail(biomeId = biome.id)
									onItemClick(destination)
								},
								painter = rememberAsyncImagePainter(biome.imageUrl),
								content = {
									Box(
										modifier = Modifier
											.fillMaxSize()
											.wrapContentHeight(Alignment.CenterVertically)
											.wrapContentWidth(Alignment.CenterHorizontally)
									) {
										Text(
											biome.name.uppercase(),
											style = MaterialTheme.typography.bodyLarge,
											modifier = Modifier,
											color = Color.White,
											textAlign = TextAlign.Center
										)
									}
								}
							)
						}
					}
					if (uiState.material.growthTime != null) {
						TridentsDividedRow()
						DarkGlassStatCard(
							icon = Lucide.Gauge,
							label = "Growth Time",
							value = uiState.material.growthTime,
							expand = { isStatInfoExpanded1.value = !isStatInfoExpanded1.value },
							isExpanded = isStatInfoExpanded1.value,
						)
						AnimatedVisibility(
							visible = isStatInfoExpanded1.value,
							enter = expandVertically() + fadeIn(),
							exit = shrinkVertically() + fadeOut()
						) {
							Text(
								text = AnnotatedString.fromHtml("Time needed for plant to fully grow."),
								modifier = Modifier
									.padding(BODY_CONTENT_PADDING.dp)
									.fillMaxWidth(),
								style = MaterialTheme.typography.bodyLarge
							)
						}
					}
					if (uiState.material.needCultivatorGround != null) {
						Spacer(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp))
						DarkGlassStatCard(
							icon = Lucide.Gauge,
							label = "Need Cultivator?",
							value = uiState.material.needCultivatorGround,
							expand = { isStatInfoExpanded2.value = !isStatInfoExpanded2.value },
							isExpanded = isStatInfoExpanded2.value,
						)
						AnimatedVisibility(
							visible = isStatInfoExpanded2.value,
							enter = expandVertically() + fadeIn(),
							exit = shrinkVertically() + fadeOut()
						) {
							Text(
								text = AnnotatedString.fromHtml("Information if seeds need cultivator to be planted"),
								modifier = Modifier
									.padding(BODY_CONTENT_PADDING.dp)
									.fillMaxWidth(),
								style = MaterialTheme.typography.bodyLarge
							)
						}
					}
					if (uiState.trees.isNotEmpty()) {
						SlavicDivider()
						HorizontalPagerSection(
							list = uiState.trees,
							data = treesData,
							onItemClick = { clickedItemId ->
								val destination = WorldDetailDestination.TreeDetail(treeId = clickedItemId)
								onItemClick(destination)
							}
						)
					}

					if (uiState.pointsOfInterest.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.pointsOfInterest,
							data = pointsOfInterestData,
							onItemClick = { clickedItemId ->
								val destination = WorldDetailDestination.PointOfInterestDetail(pointOfInterestId = clickedItemId)
								onItemClick(destination)
							}

						)
					}
					if (uiState.tools.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.tools,
							data = toolsData,
							onItemClick = { clickedItemId ->
								val destination =
									EquipmentDetailDestination.ToolDetail(clickedItemId)
								onItemClick(destination)
							},
						)
					}
					uiState.npc?.let { npc ->
						TridentsDividedRow()
						CardImageWithTopLabel(
							onClickedItem = {
								val destination = NavigationHelper.routeToCreature(
									creatureType = npc.subCategory,
									itemId = npc.id
								)
								onItemClick(destination)
							},
							itemData = npc,
							subTitle = "NPC from whom you can buy those seeds",
							contentScale = ContentScale.Crop,
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


	ValheimVikiAppTheme {
		SeedMaterialDetailContent(
			uiState = SeedUiState(),
			onBack = {},
			onItemClick = {}

			)
	}

}
