package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
import com.rabbitv.valheimviki.presentation.detail.biome.viewmodel.BiomeDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val biomeUiState by viewModel.biomeUiState.collectAsStateWithLifecycle()
	val onToggleFavorite = { favorite: Favorite, isFavorite: Boolean ->
		viewModel.toggleFavorite(
			favorite = favorite,
			currentIsFavorite = isFavorite
		)
	}

	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")

	BiomeDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		sharedTransitionScope = sharedTransitionScope,
		animatedVisibilityScope = animatedVisibilityScope,
		uiState = biomeUiState,
		onToggleFavorite = onToggleFavorite,
		vmStart = { viewModel.startContent() },
		biomeId = viewModel.biomeId.value,
		headerImageUrl = viewModel.initialImageUrl.value,
		headerTitle = viewModel.initialTitle.value
	)

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: (favorite: Favorite, currentIsFavorite: Boolean) -> Unit,
	uiState: BiomeDetailUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
	vmStart: () -> Unit,
	biomeId: String,
	headerImageUrl: String,
	headerTitle: String

) {
	val isAnimating = animatedVisibilityScope.transition.isRunning
	LaunchedEffect(isAnimating) {
		if (!isAnimating) {
			withFrameNanos { }
			vmStart()
		}
	}
	val scrollState = rememberScrollState()
	val handleClick = remember(onItemClick) {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	val sectionConfigs = remember(
		uiState.relatedCreatures,
		uiState.relatedOreDeposits,
		uiState.relatedMaterials,
		uiState.relatedPointOfInterest,
		uiState.relatedTrees
	) {
		listOf(
			uiState.relatedCreatures to HorizontalPagerData(
				title = "Creatures",
				subTitle = "Creatures you may encounter in this biome",
				icon = Lucide.PawPrint,
				iconRotationDegrees = -85f,
				itemContentScale = ContentScale.Crop
			),
			uiState.relatedOreDeposits to HorizontalPagerData(
				title = "Ore Deposits",
				subTitle = "Ore Deposits you may encounter in this biome",
				icon = Lucide.Pickaxe,
				iconRotationDegrees = 0f,
				itemContentScale = ContentScale.Crop
			),
			uiState.relatedMaterials to HorizontalPagerData(
				title = "Materials",
				subTitle = "Unique materials",
				icon = Lucide.Gem,
				iconRotationDegrees = 0f,
				itemContentScale = ContentScale.Crop
			),
			uiState.relatedPointOfInterest to HorizontalPagerData(
				title = "Points Of Interest",
				subTitle = "Points Of Interest you may encounter",
				icon = Lucide.House,
				iconRotationDegrees = 0f,
				itemContentScale = ContentScale.Crop
			),
			uiState.relatedTrees to HorizontalPagerData(
				title = "Trees",
				subTitle = "Trees you may encounter",
				icon = Lucide.Trees,
				iconRotationDegrees = 0f,
				itemContentScale = ContentScale.Crop
			)
		)
	}


	Scaffold(
		content = { padding ->
			BgImage()
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding)
			) {
				Column(
					modifier = Modifier
						.testTag("BiomeDetailScreen")
						.fillMaxSize()
						.verticalScroll(scrollState, enabled = !isAnimating),
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.Start,
				) {
					MainDetailImageAnimated(
						sharedTransitionScope = sharedTransitionScope,
						animatedVisibilityScope = animatedVisibilityScope,
						id = biomeId,
						imageUrl = headerImageUrl,
						title = headerTitle
					)

					DetailExpandableText(
						text = uiState.biome?.description ?: "",
						boxPadding = BODY_CONTENT_PADDING.dp
					)


					when (val bossState = uiState.mainBoss) {
						is UIState.Error -> {}
						is UIState.Loading -> {
							LoadingIndicator(PaddingValues(16.dp))
						}

						is UIState.Success -> {
							bossState.data?.let { boss ->
								SlavicDivider()
								ImageWithTopLabel(
									itemData = boss,
									subTitle = "BOSS",
									onItemClick = handleClick
								)
							}
						}
					}
					sectionConfigs.forEach { (state, config) ->
						when (state) {
							is UIState.Loading -> {
								TridentsDividedRow()
								LoadingIndicator(PaddingValues(16.dp))
							}

							is UIState.Success -> {
								state.data.takeIf { (it).isNotEmpty() }?.let { list ->
									TridentsDividedRow()
									HorizontalPagerSection(
										list = list,
										data = config,
										onItemClick = handleClick
									)
								}
							}

							is UIState.Error -> {}
						}
					}
					SlavicDivider()




					Spacer(Modifier.height(45.dp))
				}


				if (!isAnimating && uiState.biome != null) {
					AnimatedBackButton(
						Modifier
							.align(Alignment.TopStart)
							.padding(16.dp),
						scrollState = scrollState,
						onBack = onBack
					)
					FavoriteButton(
						Modifier
							.align(Alignment.TopEnd)
							.padding(16.dp),
						isFavorite = uiState.isFavorite,
						onToggleFavorite = {
							onToggleFavorite(
								uiState.biome.toFavorite(),
								uiState.isFavorite
							)
						},
					)
				}
			}
		}
	)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("BiomeDetailContent", showBackground = true)
@Composable
fun PreviewBiomeDetailContent() {
	val fakeBiome = Biome(
		id = "1",
		imageUrl = "https://via.placeholder.com/600x320.png?text=Biome+Image",
		name = "Przykładowy Biome",
		description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
		category = "BIOME",
		order = 1
	)
	val fakeMainBoss = MainBoss(
		id = "boss1",
		imageUrl = "https://via.placeholder.com/400x200.png?text=MainBoss+Image",
		category = "CREATURE",
		subCategory = "BOSS",
		name = "Przykładowy MainBoss",
		description = "Przykładowy opis głównego bossa.",
		order = 1,
		baseHP = 1500,
		weakness = "Ogień",
		resistance = "Lód",
		baseDamage = "100",
		collapseImmune = "False",
		forsakenPower = "High"
	)

	val uiState = BiomeDetailUiState(
		biome = fakeBiome,
		mainBoss = UIState.Loading,
		relatedCreatures = UIState.Loading,
		relatedOreDeposits = UIState.Loading,
		relatedMaterials = UIState.Loading,
		relatedPointOfInterest = UIState.Loading,
		relatedTrees = UIState.Loading,
	)

	ValheimVikiAppTheme {
		SharedTransitionLayout {
			AnimatedVisibility(visible = true) {
				BiomeDetailContent(
					onBack = { },
					sharedTransitionScope = this@SharedTransitionLayout,
					animatedVisibilityScope = this,
					uiState = uiState,
					onItemClick = { _ -> {} },
					onToggleFavorite = { _, _ -> {} },
					vmStart = {},
					biomeId = "",
					headerImageUrl = "",
					headerTitle = "",
				)
			}
		}
	}
}

