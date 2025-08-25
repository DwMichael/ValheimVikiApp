package com.rabbitv.valheimviki.presentation.detail.tree

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.expandable_text.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.button.FavoriteButton
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.DroppedItemsSection
import com.rabbitv.valheimviki.presentation.components.ui_section.UiSection
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiEvent
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeDetailUiState
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeUiEvent
import com.rabbitv.valheimviki.presentation.detail.tree.viewmodel.TreeDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TreeDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: TreeDetailScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val uiState by viewModel.treeUiState.collectAsStateWithLifecycle()
	val sharedTransitionScope = LocalSharedTransitionScope.current
		?: throw IllegalStateException("No Scope found")
	val onToggleFavorite = {
		viewModel.uiEvent(TreeUiEvent.ToggleFavorite)
	}
	TreeDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		onToggleFavorite = onToggleFavorite,
		uiState = uiState,
		sharedTransitionScope = sharedTransitionScope,
		animatedVisibilityScope = animatedVisibilityScope,
	)

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TreeDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	onToggleFavorite: () -> Unit,
	uiState: TreeDetailUiState,
	sharedTransitionScope: SharedTransitionScope,
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
	val scrollState = rememberScrollState()
	val axesData = remember {
		HorizontalPagerData(
			title = "Axes",
			subTitle = "List of axes that can cut this tree",
			icon = Lucide.Axe,
			iconRotationDegrees = 0f,
			itemContentScale = ContentScale.Crop
		)
	}
	val handleClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
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
						.testTag("TreeDetailScreen")
						.fillMaxSize()
						.verticalScroll(scrollState, enabled = !isRunning),
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.Start,
				) {
					MainDetailImageAnimated(
						sharedTransitionScope = sharedTransitionScope,
						animatedVisibilityScope = animatedVisibilityScope,
						id = uiState.tree?.id ?: "",
						imageUrl = uiState.tree?.imageUrl ?: "",
						title = uiState.tree?.name ?: "",
					)

					DetailExpandableText(
						text = uiState.tree?.description ?: "",
						boxPadding = BODY_CONTENT_PADDING.dp
					)
					UiSection(
						state = uiState.relatedBiomes,
						divider = { SlavicDivider() }
					) { data ->

						Text(
							modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
							text = "PRIMARY SPAWNS",
							textAlign = TextAlign.Left,
							style = MaterialTheme.typography.titleSmall,
							maxLines = 1,
							overflow = TextOverflow.Visible
						)
						data.forEach { biome ->
							CardWithOverlayLabel(
								onClickedItem = { handleClick(biome) },
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


					UiSection(uiState.relatedAxes) { data ->
						HorizontalPagerSection(
							list = data,
							data = axesData,
							onItemClick = handleClick,
						)
					}
					UiSection(uiState.relatedMaterials) { data ->
						DroppedItemsSection(
							onItemClick = handleClick,
							list = data,
							icon = { Lucide.Gem },
							starLevel = 0,
							title = "Materials",
							subTitle = "Unique drops are obtained by cutting this tree.",
						)
						Spacer(modifier = Modifier.height(90.dp))
					}
				}

				if (!isRunning && uiState.tree != null) {
					AnimatedBackButton(
						modifier = Modifier
							.align(Alignment.TopStart)
							.padding(16.dp),
						scrollState = scrollState,
						onBack = onBack,
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

	val biomes = listOf(
		fakeBiome, fakeBiome
	)
	val uiState = TreeDetailUiState(
		tree = Tree(
			id = "1",
			imageUrl = "ss",
			category = "asda",
			name = "adasd",
			description = "sada",
			order = 1
		),
		relatedBiomes = UIState.Success(biomes),
		relatedAxes =  UIState.Success (FakeData.fakeWeaponList)
	)

	ValheimVikiAppTheme {
		SharedTransitionLayout {
			AnimatedVisibility(visible = true) {
				TreeDetailContent(
					onBack = { },
					onItemClick = {},
					onToggleFavorite = { },
					uiState = uiState,
					sharedTransitionScope = this@SharedTransitionLayout,
					animatedVisibilityScope = this,

					)
			}
		}
	}
}

