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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.DefaultGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel.OreDepositScreenViewModel
import com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel.OreDepositUIState
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.utils.Constants.BIOME_GRID_COLUMNS
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun OreDepositScreen(
    modifier: Modifier,
    onItemClick: (String) -> Unit,
    paddingValues: PaddingValues,
    viewModel: OreDepositScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {


    val oreDepositUIState: OreDepositUIState by viewModel.oreDepositUIState.collectAsStateWithLifecycle()
    val isConnection: Boolean by viewModel.isConnection.collectAsStateWithLifecycle()

    val scrollPosition = remember { mutableIntStateOf(0) }


    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .debounce(500L)
            .collectLatest { index ->
                if (index >= 0) {
                    scrollPosition.intValue = index
                }
            }
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
            when {
                oreDepositUIState.isLoading || (oreDepositUIState.oreDeposits.isEmpty() && isConnection) -> {
                    ShimmerGridEffect()
                }

                (oreDepositUIState.error != null || !isConnection) && oreDepositUIState.oreDeposits.isEmpty() -> {
                    Box(
                        modifier = Modifier.testTag("EmptyScreenOreDeposit"),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("EmptyScreenOreDeposit")
                        ) {
                            EmptyScreen(
                                errorMessage = oreDepositUIState.error
                                    ?: stringResource(R.string.no_internet_connection_ms)
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.testTag("OreDepositGrid"),
                    ) {
                        DefaultGrid(
                            modifier = Modifier,
                            items = oreDepositUIState.oreDeposits,
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




