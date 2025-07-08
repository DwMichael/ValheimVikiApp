package com.rabbitv.valheimviki.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.navigation.TopLevelDestination
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerRowEffect
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.favorite.model.UiStateFavorite
import com.rabbitv.valheimviki.presentation.favorite.viewmodel.FavoriteViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.launch


@Composable
fun FavoriteScreen(
	onBack: () -> Unit,
	onItemClick: (destination: TopLevelDestination) -> Unit,
	viewModel: FavoriteViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FavoriteScreenContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
	)

}


@Composable
fun FavoriteScreenContent(
	onBack: () -> Unit,
	onItemClick: (destination: TopLevelDestination) -> Unit,
	uiState: UiStateFavorite
) {
	val lazyListState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val backButtonVisibleState by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
	}

	val maxHeightFirst = 380.dp
	val pageWidthFirst = 220.dp
	val itemSizeFirst = 220.dp

	val maxHeightSecond = 300.dp
	val pageWidthSecond = 220.dp
	val itemSizeSecond = 180.dp

	val biomeData = HorizontalPagerData(
		title = "Biomes",
		subTitle = "",
		icon = Lucide.MountainSnow,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)

	Scaffold(
		topBar = {
			SimpleTopBar(
				modifier = Modifier,
				title = "Favorites",
				onClick = {
					onBack()
				}
			)
		},
		modifier = Modifier
			.testTag("MaterialListScaffold"),
		floatingActionButton = {
			CustomFloatingActionButton(
				showBackButton = backButtonVisibleState,
				onClick = {
					scope.launch {
						lazyListState.animateScrollToItem(0)
					}
				},
				bottomPadding = 0.dp
			)
		},
		floatingActionButtonPosition = FabPosition.End,
		content = { innerScaffoldPadding ->
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerScaffoldPadding)
					.padding(BODY_CONTENT_PADDING.dp),
				state = lazyListState,
				horizontalAlignment = Alignment.Start,
				verticalArrangement = Arrangement.Top
			) {
				item {
					when (val state = uiState.biomes) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Biome>> ->
							if (state.data.isNotEmpty()) {
								HorizontalPagerSection(
									list = state.data,
									data = biomeData,
									onItemClick = {},
									maxHeight = 380.dp,
									pageWidth = 200.dp,
									itemSize = 240.dp
								)
							}
					}
				}
				item {
					when (val state = uiState.creatures) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Creature>> ->
							if (state.data.isNotEmpty()) {
								HorizontalPagerSection(
									list = state.data,
									data = biomeData,
									onItemClick = {},
									maxHeight = maxHeightFirst,
									pageWidth = pageWidthFirst,
									itemSize = itemSizeFirst
								)
							}
					}
				}
				item {
					when (val state = uiState.materials) {
						is UiState.Loading -> ShimmerRowEffect()
						is UiState.Error -> null
						is UiState.Success<List<Material>> ->
							if (state.data.isNotEmpty()) {
								HorizontalPagerSection(
									list = state.data,
									data = biomeData,
									onItemClick = {},
									maxHeight = maxHeightSecond,
									pageWidth = pageWidthSecond,
									itemSize = itemSizeSecond
								)
							}
					}
				}
			}
		}
	)
}


@Preview("FavoriteScreenContent", showBackground = true)
@Composable
fun PreviewFavoriteScreenContent() {
	ValheimVikiAppTheme {
		FavoriteScreenContent(
			onBack = {},
			onItemClick = {},
			uiState = UiStateFavorite(
				creatures = UiState.Success(FakeData.generateFakeCreatures()),
				weapons = UiState.Success(FakeData.fakeWeaponList),
				materials = UiState.Success(FakeData.generateFakeMaterials())
			)
		)
	}
}