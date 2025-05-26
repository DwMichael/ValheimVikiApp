package com.rabbitv.valheimviki.presentation.creatures.mob_list

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Rat
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.User
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobSegmentOption
import com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel.MobListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobListScreen(
    onItemClick: (String, CreatureSubCategory) -> Unit,
    modifier: Modifier, paddingValues: PaddingValues,
    viewModel: MobListViewModel = hiltViewModel()
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
    val icons: List<ImageVector> = listOf(
        Lucide.Rat,
        Lucide.Skull,
        Lucide.User
    )

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

    LaunchedEffect(uiState.selectedSubCategory) {
        if (selectedDifferentCategory.value) {
            coroutineScope.launch {
                lazyListState.scrollToItem(0)
                scrollPosition.intValue = 0
            }
            selectedDifferentCategory.value = false
        }
    }

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("MobListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(modifier =Modifier.fillMaxSize()) {
            if ((uiState.error != null || !uiState.isConnection) && uiState.creatureList.isEmpty()) {
                EmptyScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("EmptyScreenMobList"),
                    errorMessage = uiState.error ?: "Please connect to the internet to fetch data."
                )
            } else {
                Column(
                    modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SegmentedButtonSingleSelect(
                        options = MobSegmentOption.entries,
                        selectedOption = uiState.selectedSubCategory,
                        onOptionSelected = {
                            selectedDifferentCategory.value = true
                            viewModel.onCategorySelected(it)
                        },
                        icons = icons
                    )
                    Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (uiState.isLoading && (uiState.creatureList.isEmpty() && uiState.isConnection)) {
                            Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                            ShimmerListEffect()
                        } else {
                            ListContent(
                                items = uiState.creatureList,
                                clickToNavigate = onItemClick,
                                lazyListState = lazyListState,
                                subCategoryNumber = uiState.selectedSubCategory,
                                horizontalPadding = 0.dp,
                            )
                        }
                    }
                }

                CustomFloatingActionButton(
                    backButtonVisibleState = backButtonVisibleState,
                    backToTopState = backToTopState,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(BODY_CONTENT_PADDING.dp)
                )
            }
        }
    }
}


@Preview("CustomFloatingActionButton", widthDp = 80, heightDp = 80)
@Composable
fun PreviewCustomFloatingActionButton() {
    val backButtonVisibleState = true

    val backToTopState = remember { mutableStateOf(false) }
    ValheimVikiAppTheme {
        CustomFloatingActionButton(
            backButtonVisibleState = backButtonVisibleState,
            backToTopState = backToTopState
        )
    }
}
