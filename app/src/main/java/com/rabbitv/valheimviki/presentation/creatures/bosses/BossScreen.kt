package com.rabbitv.valheimviki.presentation.creatures.bosses


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.creatures.bosses.viewmodel.BossesViewModel
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun BossScreen(
    modifier: Modifier,
    onItemClick: (destination: DetailDestination) -> Unit,
    paddingValues: PaddingValues,
    viewModel: BossesViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val mainBossUiListState: UiListState<MainBoss> by viewModel.mainBossUiListState.collectAsStateWithLifecycle()
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
                .testTag("BossSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = mainBossUiListState) {
                is UiListState.Loading -> ShimmerGridEffect()
                is UiListState.Error -> EmptyScreen(errorMessage = state.message)
                is UiListState.Success -> DefaultGrid(
                    modifier = Modifier,
                    items = state.list,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewBossListScreen() {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Biomes") })
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

            }
        }
    )
}