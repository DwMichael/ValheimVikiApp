package com.rabbitv.valheimviki.presentation.ore_deposit


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel.OreDepositScreenViewModel
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun OreDepositScreen(
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: OreDepositScreenViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	val oreDepositUIState by viewModel.uiState.collectAsStateWithLifecycle()
	val lazyGridState = rememberLazyGridState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	Box(
		modifier = modifier
	) {
		Surface(
			color = Color.Transparent,
			modifier = Modifier
				.testTag("OreDepositSurface")
				.fillMaxSize()
				.padding(paddingValues)

		) {

			when (val state = oreDepositUIState) {

				is UIState.Loading -> {
					ShimmerGridEffect()
				}

				is UIState.Error -> {
					Box(
						modifier = Modifier.testTag("EmptyScreenOreDeposit"),
					) {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.testTag("EmptyScreenOreDeposit")
						) {
							EmptyScreen(
								errorMessage = state.message
							)
						}
					}
				}

				is UIState.Success -> {
					Box(
						modifier = Modifier.testTag("OreDepositGrid"),
					) {
						DefaultGrid(
							modifier = Modifier,
							items = state.data,
							onItemClick = handleItemClick,
							numbersOfColumns = BIOME_GRID_COLUMNS,
							height = ITEM_HEIGHT_TWO_COLUMNS,
							animatedVisibilityScope = animatedVisibilityScope,
							lazyGridState = lazyGridState,
						)
					}
				}
			}
		}
	}
}




