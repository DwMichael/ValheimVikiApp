package com.rabbitv.valheimviki.presentation.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Amphora
import com.composables.icons.lucide.Anvil
import com.composables.icons.lucide.Bone
import com.composables.icons.lucide.Crown
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.Eclipse
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Ghost
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Magnet
import com.composables.icons.lucide.Mountain
import com.composables.icons.lucide.ShoppingCart
import com.composables.icons.lucide.Sprout
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.CategoryGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

class MaterialChip(
    override val option: MaterialSubType,
    override val icon: ImageVector,
    override val label: String
) : ChipData<MaterialSubType>


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialListScreen(
    onItemClick: (String) -> Unit,
    modifier: Modifier, paddingValues: PaddingValues,
    viewModel: MaterialListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )

    val backButtonVisibleState by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
    }
    val backToTopState = remember { mutableStateOf(false) }


    val icons: List<ImageVector> = listOf(
        Lucide.Bone,
        Lucide.Anvil,
        Lucide.Eclipse,
        Lucide.Ghost,
        Lucide.Gem,
        Lucide.Crown,
        Lucide.Sprout,
        Lucide.Flame,
        Lucide.Magnet,
        Lucide.ShoppingCart,
        Lucide.Star
    )

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("MaterialListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if ((uiState.error != null || !uiState.isConnection) && uiState.materialsList.isEmpty()) {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("EmptyScreenMaterialList"),
                errorMessage = uiState.error ?: "Please connect to the internet to fetch data."
            )
        } else {
            Column(
                modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading && (uiState.materialsList.isEmpty() && uiState.isConnection)) {
                        Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                        ShimmerGridEffect()
                    } else {

                        if (uiState.selectedSubCategory == null) {
                            CategoryGrid(
                                modifier = modifier,
                                items = MaterialSegmentOption.entries,
                                onItemClick = { category ->
                                    viewModel.onCategorySelected(category)
                                },
                                numbersOfColumns = 2,
                                height = 120.dp,
                                icons = icons,
                                lazyGridState = rememberLazyGridState()
                            )
                        } else {
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
                            ListContent(
                                items = uiState.materialsList,
                                clickToNavigate = { s, i -> {} },
                                lazyListState = lazyListState,
                                subCategoryNumber = 0,
                                imageScale = ContentScale.Fit,
                                horizontalPadding = 0.dp
                            )
                        }
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
private fun getChipsForCategory(category: MaterialSubCategory?): List<MaterialChip> { // Changed to nullable
    return when (category) { // category can now be null
        MaterialSubCategory.BOSS_DROP -> listOf(
            MaterialChip(
                MaterialSubType.ITEM,
                Lucide.Amphora,
                "Item"
            )
        )

        MaterialSubCategory.MINI_BOSS_DROP -> emptyList()
        MaterialSubCategory.CREATURE_DROP -> listOf(
            MaterialChip(
                MaterialSubType.TROPHY,
                Lucide.Trophy,
                "Trophy"
            )
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
            ), MaterialChip(MaterialSubType.ORES, Lucide.Mountain, "Ores")
        )

        MaterialSubCategory.SHOP -> emptyList()
        MaterialSubCategory.VALUABLE -> emptyList()
        null -> emptyList() // Handle the null case explicitly
    }
}