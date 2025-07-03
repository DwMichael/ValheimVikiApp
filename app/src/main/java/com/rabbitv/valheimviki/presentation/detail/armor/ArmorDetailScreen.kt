package com.rabbitv.valheimviki.presentation.detail.armor


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.UpgradeArmorInfo
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.card.RequiredMaterialColumn
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.armor.viewmodel.ArmorDetailViewModel
import com.rabbitv.valheimviki.presentation.detail.weapon.model.ArmorUiState
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen20Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.ui.theme.YellowDTNotSelected
import com.rabbitv.valheimviki.utils.mapUpgradeArmorInfoToGridList


@Composable
fun ArmorDetailScreen(
	onBack: () -> Unit,
	viewModel: ArmorDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	ArmorDetailContent(
		onBack = onBack,
		uiState = uiState
	)
}

@Composable
fun ArmorDetailContent(
	onBack: () -> Unit,
	uiState: ArmorUiState
) {

	val isExpandable = remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()
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
		uiState.armor?.let { armor ->
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
			) {
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
					FramedImage(armor.imageUrl)
					Text(
						armor.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					armor.description?.let {
						DetailExpandableText(
							text = it,
							collapsedMaxLine = 3,
							isExpanded = isExpandable,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
					}

					uiState.craftingObjects?.let { craftingStation ->
						TridentsDividedRow()
						CardImageWithTopLabel(
							itemData = craftingStation,
							subTitle = "Crafting Station Needed to Make This Item",
							contentScale = ContentScale.Fit,
						)
					}
					TridentsDividedRow()
					if (!armor.upgradeInfoList.isNullOrEmpty()) {
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

						armor.upgradeInfoList.forEachIndexed { levelIndex, upgradeInfoForLevel ->
							val upgradeStats = mapUpgradeArmorInfoToGridList(upgradeInfoForLevel)
							LevelInfoCard(
								modifier = Modifier.padding(
									horizontal = BODY_CONTENT_PADDING.dp,
									vertical = 8.dp
								),
								level = levelIndex,
								upgradeStats = upgradeStats,
								materialsForUpgrade = uiState.materials,
							)
						}
						SlavicDivider()
					} else if (uiState.materials.isNotEmpty()) {

						RequiredMaterialColumn(
							level = 0,
							foodForUpgrade = emptyList(),
							materialsForUpgrade = uiState.materials
						)

					}
					armor.effects?.let { effectContent ->
						if (effectContent.isNotBlank()) {
							InfoSection(
								title = "Additional Effect",
								content = effectContent
							)
						}
					}

					armor.usage?.let { usageContent ->
						if (usageContent.isNotBlank()) {
							InfoSection(
								title = "Usage",
								content = usageContent
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
}

@Composable
fun InfoSection(
	title: String,
	content: String,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 8.dp)
			.border(
				width = 1.dp,
				color = YellowDTBorder,
				shape = RoundedCornerShape(8.dp)
			)
			.background(
				color = ForestGreen20Dark,
				shape = RoundedCornerShape(8.dp)
			)
			.padding(horizontal = 16.dp, vertical = 12.dp)
	) {
		Text(
			text = title,
			color = PrimaryWhite,
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold
		)
		Spacer(modifier = Modifier.height(8.dp))
		Text(
			text = content,
			color = YellowDTNotSelected,
			style = MaterialTheme.typography.bodyLarge,
			lineHeight = 22.sp
		)
	}
}

@Preview(name = "PreviewArmorDetailScreen", showBackground = true)
@Composable
private fun PreviewArmorDetailScreen() {

	val testArmor = Armor(
		id = "test_helmet",
		imageUrl = "",
		category = "Headgear",
		subCategory = "Helmet",
		name = "Hardcoded Test Helmet",
		description = "This is a hardcoded description to test the preview.",
		upgradeInfoList = listOf(
			UpgradeArmorInfo(
				armor = 35,
				durability = 120,
				stationLevel = 2
			)
		),
		effects = "Hardcoded Effect: +10 Magic",
		usage = "Hardcoded Usage: Used for testing.",
		fullSetEffects = "Hardcoded Full Set Bonus.",
		order = 1
	)

	ValheimVikiAppTheme {
		ArmorDetailContent(
			onBack = {},
			uiState = ArmorUiState(
				armor = testArmor,
				materials = emptyList(),
				craftingObjects = CraftingObject(
					id = "1",
					imageUrl = "",
					category = "",
					subCategory = "TODO()",
					name = "Workbench",
					description = "",
					order = 1
				),
				isLoading = false,
				error = null
			)
		)
	}
}