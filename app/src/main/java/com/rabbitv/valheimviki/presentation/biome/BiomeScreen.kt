package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.presentation.biome.viewmodel.BiomeScreenViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(FlowPreview::class)
@Composable
fun BiomeScreen(
    modifier: Modifier,
    onItemClick: (String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: BiomeScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val biomeUiListState: UiListState<Biome> by viewModel.biomeUiListState.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState()

    Box(
        modifier = modifier
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .testTag("BiomeSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = biomeUiListState) {
                is UiListState.Loading -> ShimmerGridEffect()
                is UiListState.Error -> EmptyScreen(errorMessage = state.message.toString())
                is UiListState.Success -> DefaultGrid(
                    modifier = Modifier,
                    items = state.list,
                    onItemClick = onItemClick,
                    numbersOfColumns = BIOME_GRID_COLUMNS,
                    height = ITEM_HEIGHT_TWO_COLUMNS,
                    animatedVisibilityScope = animatedVisibilityScope,
                    lazyGridState = lazyGridState,
                )
            }
        }
    }
}




