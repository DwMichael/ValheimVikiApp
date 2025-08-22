package com.rabbitv.valheimviki.presentation.detail.material.mob_drop


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Cat
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model.BossDropUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model.MobDropUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model.MobDropUiState
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.viewmodel.MobDropDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@Composable
fun MobDropDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: MobDropDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onToggleFavorite = {
		viewModel.uiEvent(MobDropUiEvent.ToggleFavorite)
	}
	MobDropDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
	)

}



@Composable
fun MobDropDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: MobDropUiState,
) {

	val scrollState = rememberScrollState()
	remember { mutableIntStateOf(0) }

	val isExpandable = remember { mutableStateOf(false) }
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val aggressiveCreatureData = HorizontalPagerData(
		title = "Aggressive Creatures",
		subTitle = "Creatures from witch this material drop",
		icon = Lucide.Cat,
		iconRotationDegrees = 0f,
		itemContentScale = ContentScale.Crop
	)
	val passiveCreatureData = HorizontalPagerData(
		title = "Passive Creatures",
		subTitle = "Creatures from witch this material drop",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)
	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi from witch this material drop",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
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
					if (uiState.aggressive.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.aggressive,
							data = aggressiveCreatureData,
							onItemClick = handleItemClick,
						)
					}
					if (uiState.passive.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.passive,
							data = passiveCreatureData,
							onItemClick = handleItemClick,
						)
					}
					if (uiState.pointsOfInterest.isNotEmpty()) {
						TridentsDividedRow()
						HorizontalPagerSection(
							list = uiState.pointsOfInterest,
							data = pointsOfInterestData,
							onItemClick = handleItemClick
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
			uiState.material?.let { material ->
				FavoriteButton(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(16.dp),
					isFavorite = uiState.isFavorite,
					onToggleFavorite = {
						onToggleFavorite()
					}
				)
			}
		}
	}
}


@Preview("ToolDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {

	val agg = listOf(
		Creature(
			id = "creature002",
			category = "Aggressive",
			subCategory = "Undead",
			imageUrl = "https://example.com/frost_draugr.png",
			name = "Frost Draugr",
			description = "An ancient warrior risen from the dead in the frozen mountains. Carries ice-encrusted weapons and armor.",
			order = 2,
			levels = 2,
			baseHP = 150,
			weakness = "Fire, Spirit",
			resistance = "Frost, Poison",
			baseDamage = "35-45",
		)
	)
	ValheimVikiAppTheme {
		MobDropDetailContent(
			uiState = MobDropUiState(
				material = FakeData.generateFakeMaterials()[0],
				aggressive = agg.toAggressiveCreatures(),
				passive = FakeData.generateFakeCreatures().toPassiveCreatures(),
				isLoading = false,
				error = null
			),
			onBack = {},
			onItemClick = {},
			onToggleFavorite = { }
		)
	}

}
