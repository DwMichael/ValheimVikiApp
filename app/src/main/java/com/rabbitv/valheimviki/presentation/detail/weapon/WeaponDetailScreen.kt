package com.rabbitv.valheimviki.presentation.detail.weapon


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel.WeaponDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.fakeWeaponList
import com.rabbitv.valheimviki.utils.mapUpgradeInfoToGridList


@Composable
fun WeaponDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: WeaponDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(WeaponDetailUiEvent.ToggleFavorite)
	}
	WeaponDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState
	)
}

@Composable
fun WeaponDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: WeaponUiState
) {
	val scrollState = rememberScrollState()
	val handleClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	BgImage()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentColor = PrimaryWhite
	) { innerPadding ->
		uiState.weapon?.let { weapon ->
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
					FramedImage(weapon.imageUrl)
					Text(
						weapon.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()
					weapon.description?.let {
						DetailExpandableText(
							text = it,
							collapsedMaxLine = 3,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
					}



					when (val materials = uiState.materials) {
						is UIState.Error -> {}
						is UIState.Loading -> {
							TridentsDividedRow()
							LoadingIndicator(
								paddingValues = PaddingValues(16.dp)
							)
						}

						is UIState.Success -> {
							val levelsCount =
								(weapon.upgradeInfoList?.size ?: 0).takeIf { it > 0 } ?: 1
							if (weapon.upgradeInfoList?.isNotEmpty() == true) {
								TridentsDividedRow()
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
							}


							for (levelIndex in 0 until levelsCount) {
								val upgradeStats = weapon.upgradeInfoList?.getOrNull(levelIndex)
									?.let { mapUpgradeInfoToGridList(it) } ?: emptyList()

								LevelInfoCard(
									modifier = Modifier.padding(
										horizontal = BODY_CONTENT_PADDING.dp,
										vertical = 8.dp
									),
									onItemClick = handleClick,
									level = levelIndex,
									upgradeStats = upgradeStats,
									materialsForUpgrade = materials.data,
									foodForUpgrade = when (val foodState =
										uiState.foodAsMaterials) {
										is UIState.Success -> foodState.data
										else -> emptyList()
									},
								)
							}
						}
					}
					when (val state = uiState.relatedPointOfInterest) {
						is UIState.Loading -> {
							TridentsDividedRow()
							LoadingIndicator(PaddingValues(16.dp))
						}

						is UIState.Success -> {
							state.data.takeIf { (it).isNotEmpty() }?.let { list ->
								TridentsDividedRow()
								HorizontalPagerSection(
									list = list,
									data = HorizontalPagerData(
										title = "Points Of Interest",
										subTitle = "Point Of Interest Where You Can Find This Object",
										icon = Lucide.House,
										iconRotationDegrees = 0f,
										itemContentScale = ContentScale.Crop
									),
									onItemClick = handleClick
								)
							}
						}

						is UIState.Error -> {}

					}
					when (val state = uiState.craftingObjects) {
						is UIState.Error -> {}
						UIState.Loading -> {
							TridentsDividedRow()
							LoadingIndicator(PaddingValues(16.dp))
						}

						is UIState.Success -> {
							TridentsDividedRow()
							state.data?.let { craftingStation ->
								CardImageWithTopLabel(
									onClickedItem = handleClick,
									itemData = craftingStation,
									subTitle = "Crafting Station Needed to Make This Item",
									contentScale = ContentScale.FillBounds,
								)
							}
						}
					}
					Spacer(modifier = Modifier.height(45.dp))
				}

				AnimatedBackButton(
					modifier = Modifier
						.align(Alignment.TopStart)
						.padding(16.dp),
					scrollState = scrollState,
					onBack = onBack
				)
				FavoriteButton(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(16.dp),
					isFavorite = uiState.isFavorite,
					onToggleFavorite = {
						onToggleFavorite()
					},
				)
			}
		}
	}
}


@Preview(name = "WeaponDetailScreen", showBackground = true)
@Composable
private fun PreviewWeaponDetailScreen() {
	ValheimVikiAppTheme {
		WeaponDetailContent(
			onBack = {},
			onItemClick = {},
			uiState = WeaponUiState(
				weapon = fakeWeaponList[0],
				materials = UIState.Success(emptyList()),
				foodAsMaterials = UIState.Success(emptyList()),
				craftingObjects = UIState.Success(
					CraftingObject(
						id = "1",
						imageUrl = "",
						category = "",
						subCategory = "TODO()",
						name = "Workbench",
						description = "",
						order = 1
					)
				)
			),
			onToggleFavorite = {}
		)
	}

}
