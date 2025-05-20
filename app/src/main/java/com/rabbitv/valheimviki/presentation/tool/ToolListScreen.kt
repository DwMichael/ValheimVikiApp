package com.rabbitv.valheimviki.presentation.tool

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Diamond
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.TestTubeDiagonal
import com.composables.icons.lucide.Wheat
import com.composables.icons.lucide.Wrench
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.tool.ToolSubCategory
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.ShimmerEffect
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.tool.model.ToolChip
import com.rabbitv.valheimviki.presentation.tool.viewmodel.ToolListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolListScreen(
    onItemClick: (String, ToolSubCategory) -> Unit,
    modifier: Modifier,
    paddingValues: PaddingValues,
    viewModel: ToolListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDifferentCategory = remember { mutableStateOf(false) }
    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )
    val coroutineScope = rememberCoroutineScope()
    val backButtonVisibleState by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
    }
    val backToTopState = remember { mutableStateOf(false) }


    if (backToTopState.value) {
        LaunchedEffect(backToTopState.value) {
            lazyListState.animateScrollToItem(0)
            scrollPosition.intValue = 0
            backToTopState.value = false
        }
    }

    LaunchedEffect(lazyListState) {
        coroutineScope.launch {
            if (lazyListState.firstVisibleItemIndex >= 0) {
                scrollPosition.intValue = lazyListState.firstVisibleItemIndex
            }
        }
    }


    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("FoodListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if ((uiState.error != null || !uiState.isConnection) && uiState.toolList.isEmpty()) {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("EmptyScreenFoodList"),
                errorMessage = uiState.error ?: "Please connect to the internet to fetch data."
            )
        } else {
            Column(
                modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchFilterBar(
                    chips = getChipsForCategory(),
                    selectedOption = uiState.selectedChip,
                    onSelectedChange = { _, subCategory ->
                        if (uiState.selectedChip == subCategory) {
                            viewModel.onChipSelected(null)
                        } else {
                            viewModel.onChipSelected(subCategory)
                        }
                    },
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading && (uiState.toolList.isEmpty() && uiState.isConnection)) {
                        Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                        ShimmerEffect()
                    } else {
                        ListContent(
                            items = uiState.toolList,
                            clickToNavigate = onItemClick,
                            lazyListState = lazyListState,
                            subCategoryNumber = ToolSubCategory.BUILDING,
                            horizontalPadding = 0.dp,
                            imageScale = ContentScale.Fit
                        )
                    }
                }
            }

            CustomFloatingActionButton(
                backButtonVisibleState = backButtonVisibleState,
                backToTopState = backToTopState
            )
        }
    }
}


@Composable
private fun getChipsForCategory(): List<ToolChip> {
    return listOf(
        ToolChip(
            ToolSubCategory.MEAD_CONSUMPTION,
            Lucide.TestTubeDiagonal,
            "Mead consumption"
        ),
        ToolChip(
            ToolSubCategory.BUILDING,
            Lucide.Wrench,
            "Building"
        ),
        ToolChip(
            ToolSubCategory.PICKAXES,
            Lucide.Pickaxe,
            "Pickaxe"
        ),
        ToolChip(
            ToolSubCategory.TRAVERSAL,
            Lucide.Diamond,
            "Accessories"
        ),
        ToolChip(
            ToolSubCategory.FARMING,
            Lucide.Wheat,
            "Farming"
        ),
        ToolChip(
            ToolSubCategory.FISHING,
            ImageVector.vectorResource(id = R.drawable.fishing_rod),
            "Fishing"
        ),
    )
}
