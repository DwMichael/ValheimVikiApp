package com.rabbitv.valheimviki.presentation.detail.building_material


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Gauge
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.building_material.model.BuildingMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.building_material.viewmodel.BuildingMaterialDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.YellowDT

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BuildingMaterialDetailScreen(
	onBack: () -> Unit,
	viewModel: BuildingMaterialDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	BuildingMaterialDetailContent(
		uiState = uiState,
		onBack = onBack,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BuildingMaterialDetailContent(
	uiState: BuildingMaterialUiState,
	onBack: () -> Unit,
) {

	val scrollState = rememberScrollState()
	val previousScrollValue = remember { mutableIntStateOf(0) }

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

	val isStatInfoExpanded1 = remember { mutableStateOf(false) }
	val isExpandable = remember { mutableStateOf(false) }
	val painterBackgroundImage = painterResource(R.drawable.main_background)

	val comfortDescription =
		"<p><b>Comfort</b> level determines the duration of <a href=\"/wiki/Resting_Effect\" class=\"mw-redirect\" title=\"Resting Effect\">Resting Effect</a>. Base duration is 8 minutes, with each comfort level stacking 1 minute up to 24 minutes with usual items and 26 minutes with rare seasonal items.  \n" +
				"</p><p>The max comfort reachable normally is 17. If near a <a href=\"/wiki/Maypole\" title=\"Maypole\">Maypole</a> 18 or with the seasonal <a href=\"/wiki/Yule_tree\" title=\"Yule tree\">Yule tree</a> 19.  \n" +
				"</p><p>When entering most dungeons (such as <a href=\"/wiki/Burial_Chambers\" title=\"Burial Chambers\">Burial Chambers</a>) a fire can be constructed to easily gain Comfort 3 (and a 10-minute <a href=\"/wiki/Rested_Effect\" class=\"mw-redirect\" title=\"Rested Effect\">Rested Effect</a>).  \n" +
				"</p><p>Note that while you can add more than one furniture item from the categories below, only the one with the highest comfort will influence the comfort rating. \n</p>"

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
			uiState.buildingMaterial?.let { buildingMaterial ->
				Column(
					modifier = Modifier
						.verticalScroll(rememberScrollState())
						.padding(
							top = 20.dp,
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
					SlavicDivider()

					if (!buildingMaterial.description.isBlank()) {
						DetailExpandableText(
							text = buildingMaterial.description,
							boxPadding = BODY_CONTENT_PADDING.dp,
							isExpanded = isExpandable
						)
						SlavicDivider()
					}
					if (uiState.buildingMaterial.comfortLevel != null) {
						DarkGlassStatCard(
							Lucide.Gauge,
							"Comfort Level",
							uiState.buildingMaterial.comfortLevel.toString(),
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
						TridentsDividedRow()
						uiState.craftingStation.forEach { craftingStation ->
							CardImageWithTopLabel(
								itemData = craftingStation,
								subTitle = "Crafting station that must be near the construction",
								contentScale = ContentScale.Fit,
							)
							Spacer(modifier = Modifier.padding(BODY_CONTENT_PADDING.dp))
						}
					}

					LazyVerticalGrid(
						modifier = Modifier
							.height(400.dp)
							.fillMaxWidth(),
						state = rememberLazyGridState(),
						columns = GridCells.Fixed(2),
						horizontalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
						verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
						contentPadding = PaddingValues(bottom = 70.dp),
					) {
						items(uiState.materials) { item ->
							ElevatedCard(
								onClick = {},
								modifier = Modifier
									.fillMaxWidth()
									.height(150.dp),
								shape = RoundedCornerShape(SMALL_PADDING),
								colors = CardDefaults.cardColors(
									containerColor = YellowDT
								),
								elevation = CardDefaults.elevatedCardElevation(2.dp)
							) {
								Column(
									modifier = Modifier
										.fillMaxSize(),
									verticalArrangement = Arrangement.Top,
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									AsyncImage(
										modifier = Modifier
											.fillMaxWidth()
											.height(160.dp)
											.clip(RoundedCornerShape(SMALL_PADDING)),
										model = ImageRequest.Builder(context = LocalContext.current)
											.data(item.imageUrl)
											.crossfade(true)
											.build(),
										error = painterResource(R.drawable.ic_placeholder),
										placeholder = painterResource(R.drawable.ic_placeholder),
										contentDescription = stringResource(R.string.item_grid_image),
										contentScale = ContentScale.Crop,
									)
									Row(
										modifier = Modifier
											.fillMaxWidth()
											.wrapContentHeight()
											.padding(
												8.dp
											),

										) {

										Text(
											text = item.name.uppercase(),
											maxLines = 2,
											textAlign = TextAlign.Start,
											color = PrimaryWhite,
											style = MaterialTheme.typography.titleMedium
										)
										Spacer(Modifier.size(8.dp))
									}

								}
							}

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
