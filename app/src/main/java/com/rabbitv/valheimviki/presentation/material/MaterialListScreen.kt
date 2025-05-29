package com.rabbitv.valheimviki.presentation.material

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Amphora
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mountain
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.launch

class MaterialChip(
    override val option: MaterialSubType,
    override val icon: ImageVector,
    override val label: String
) : ChipData<MaterialSubType>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialListScreen(
    onBackClick: () -> Unit,
    onItemClick: (String, Int) -> Unit,
    viewModel: MaterialListViewModel,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )
    val scope = rememberCoroutineScope()
    val title = uiState.selectedSubCategory?.let { viewModel.getLabelFor(it) }
    val backButtonVisibleState by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
    }

    BackHandler(onBack = {
        viewModel.onCategorySelected(null)
        viewModel.onTypeSelected(null)
        onBackClick()
    })
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.onCategorySelected(null)
                        viewModel.onTypeSelected(null)
                        onBackClick()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = BODY_CONTENT_PADDING.dp,
                            end = BODY_CONTENT_PADDING.dp
                        ),
                    colors = IconButtonColors(
                        containerColor = ForestGreen10Dark,
                        contentColor = PrimaryWhite,
                        disabledContainerColor = Color.Black,
                        disabledContentColor = Color.Black,
                    ),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                if (title != null) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        },
        modifier = Modifier
            .testTag("MaterialListScaffold"),
        floatingActionButton = {
            CustomFloatingActionButton(
                showBackButton = backButtonVisibleState,
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerScaffoldPadding ->
            if ((uiState.error != null || !uiState.isConnection) && uiState.materialsList.isEmpty()) {
                EmptyScreen(
                    errorMessage = uiState.error
                        ?: "Please connect to the internet to fetch data."
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerScaffoldPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(BODY_CONTENT_PADDING.dp)
                    ) {
                        if (uiState.isLoading && (uiState.materialsList.isEmpty() && uiState.isConnection)) {
                            Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                            ShimmerListEffect()
                        } else {
                            Column(

                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (uiState.selectedSubCategory != null) {
                                    SearchFilterBar(
                                        chips = getChipsForCategory(uiState.selectedSubCategory),
                                        selectedOption = uiState.selectedSubType,
                                        onSelectedChange = { _, subCategory ->
                                            if (uiState.selectedSubType == subCategory) {
                                                viewModel.onTypeSelected(null)
                                            } else {
                                                viewModel.onTypeSelected(subCategory)
                                            }
                                        },
                                        modifier = Modifier,
                                    )
                                    Spacer(
                                        Modifier.padding(
                                            horizontal = BODY_CONTENT_PADDING.dp,
                                            vertical = 5.dp
                                        )
                                    )
                                }

                                ListContent(
                                    items = uiState.materialsList,
                                    clickToNavigate = onItemClick,
                                    lazyListState = lazyListState,
                                    subCategoryNumber = 0,
                                    imageScale = ContentScale.Fit,
                                    horizontalPadding = 0.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
private fun getChipsForCategory(category: MaterialSubCategory?): List<MaterialChip> { // Changed to nullable
    return when (category) { // category can now be null
        MaterialSubCategory.BOSS_DROP -> listOf(
            MaterialChip(
                MaterialSubType.ITEM,
                Lucide.Amphora,
                "Item"
            ),
            MaterialChip(
                MaterialSubType.TROPHY,
                Lucide.Trophy,
                "Trophy"
            )
        )

        MaterialSubCategory.MINI_BOSS_DROP -> emptyList()
        MaterialSubCategory.CREATURE_DROP -> listOf(
            MaterialChip(
                MaterialSubType.TROPHY,
                Lucide.Trophy,
                "Trophy"
            ),
        )

        MaterialSubCategory.FORSAKEN_ALTAR_OFFERING -> emptyList()
        MaterialSubCategory.CRAFTED -> emptyList()
        MaterialSubCategory.MISCELLANEOUS -> emptyList()
        MaterialSubCategory.GEMSTONE -> emptyList()
        MaterialSubCategory.SEED -> emptyList()
        MaterialSubCategory.METAL -> listOf(
            MaterialChip(
                MaterialSubType.INGOTS,
                Lucide.Cuboid,
                "Ingots"
            ),
            MaterialChip(MaterialSubType.ORES, Lucide.Mountain, "Ores")
        )

        MaterialSubCategory.SHOP -> emptyList()
        MaterialSubCategory.VALUABLE -> emptyList()
        null -> emptyList() // Handle the null case explicitly
    }
}