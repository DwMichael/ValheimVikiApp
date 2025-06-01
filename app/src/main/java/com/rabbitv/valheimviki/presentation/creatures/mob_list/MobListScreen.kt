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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Rat
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.User
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobSegmentOption
import com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel.MobListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun MobListScreen(
    onItemClick: (String, CreatureSubCategory) -> Unit,
    modifier: Modifier, paddingValues: PaddingValues,
    viewModel: MobListViewModel = hiltViewModel()
) {
    val icons: List<ImageVector> = listOf(
        Lucide.Rat,
        Lucide.Skull,
        Lucide.User
    )
    val uiState by viewModel.mobUiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val backButtonVisibleState by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
    }

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("MobListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SegmentedButtonSingleSelect(
                    options = MobSegmentOption.entries,
                    selectedOption = uiState.selectedCategory,
                    onOptionSelected = {
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                        viewModel.onCategorySelected(it)
                    },
                    icons = icons
                )
                Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    when (val state = uiState) {
                        is UiCategoryState.Error<CreatureSubCategory> -> EmptyScreen(errorMessage = state.message.toString())
                        is UiCategoryState.Loading<CreatureSubCategory> -> ShimmerGridEffect()
                        is UiCategoryState.Success<CreatureSubCategory, Creature> -> ListContent(
                            items = state.list,
                            clickToNavigate = onItemClick,
                            lazyListState = lazyListState,
                            subCategoryNumber = state.selectedCategory,
                            horizontalPadding = 0.dp,
                        )
                    }
                }
            }
            CustomFloatingActionButton(
                showBackButton = backButtonVisibleState,
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BODY_CONTENT_PADDING.dp)
            )
        }
    }
}


