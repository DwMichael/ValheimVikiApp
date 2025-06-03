package com.rabbitv.valheimviki.presentation.tree


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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.tree.viewmodel.TreeScreenViewModel
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun TreeScreen(
    modifier: Modifier,
    onItemClick: (String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: TreeScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {


    val treeUIState: UiListState<Tree> by viewModel.uiState.collectAsStateWithLifecycle()


    val lazyGridState = rememberLazyGridState()



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
            when (val state = treeUIState) {

                is UiListState.Loading -> {
                    ShimmerGridEffect()
                }

                is UiListState.Error -> {
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

                is UiListState.Success -> {
                    Box(
                        modifier = Modifier.testTag("OreDepositGrid"),
                    ) {
                        DefaultGrid(
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
    }
}




