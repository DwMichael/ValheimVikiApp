package com.rabbitv.valheimviki.presentation.building_material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Archive
import com.composables.icons.lucide.Armchair
import com.composables.icons.lucide.Box
import com.composables.icons.lucide.Cog
import com.composables.icons.lucide.Flag
import com.composables.icons.lucide.Grid2x2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sailboat
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Table
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.floating_action_button.CustomFloatingActionButton
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite

class BuildingMaterialChip(
    override val option: BuildingMaterialSubType,
    override val icon: ImageVector,
    override val label: String
) : ChipData<BuildingMaterialSubType>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingMaterialListScreen(
    onBackClick: () -> Unit,
    onItemClick: (String, Int) -> Unit,
    viewModel: BuildingMaterialListViewModel,
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
    if (backToTopState.value) {
        LaunchedEffect(backToTopState.value) {
            lazyListState.animateScrollToItem(0)
            scrollPosition.intValue = 0
            backToTopState.value = false
        }
    }
    Scaffold(
        topBar = {
            IconButton(
                onClick = {
                    viewModel.onCategorySelected(null)
                    viewModel.onTypeSelected(null)
                    onBackClick()
                },
                modifier = Modifier.padding(
                    start = BODY_CONTENT_PADDING.dp,
                    end = BODY_CONTENT_PADDING.dp,
                    top = 40.dp,
                    bottom = 0.dp,
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
        },
        modifier = Modifier
            .testTag("BuildingMaterialListScaffold"),
        floatingActionButton = {
            CustomFloatingActionButton(
                backButtonVisibleState = backButtonVisibleState,
                backToTopState = backToTopState
            )
        },
        content = { innerScaffoldPadding ->
            if ((uiState.error != null || !uiState.isConnection) && uiState.buildingMaterialList.isEmpty()) {
                EmptyScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("EmptyScreenBuildingMaterialList")
                        .padding(innerScaffoldPadding),
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
                        if (uiState.isLoading && (uiState.buildingMaterialList.isEmpty() && uiState.isConnection)) {
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
                                    items = uiState.buildingMaterialList,
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
private fun getChipsForCategory(category: BuildingMaterialSubCategory?): List<BuildingMaterialChip> { // Changed to nullable
    return when (category) { // category can now be null
        BuildingMaterialSubCategory.STONE_AND_METAL -> emptyList()
        BuildingMaterialSubCategory.LIGHT_SOURCE -> emptyList()
        BuildingMaterialSubCategory.FURNITURE -> listOf(
            BuildingMaterialChip(
                BuildingMaterialSubType.STORAGE,
                Lucide.Archive,
                "Storage"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.FUNCTIONAL,
                Lucide.Cog,
                "Functional"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.TABLE,
                Lucide.Table,
                "Table"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.CHAIR,
                Lucide.Armchair,
                "Chair"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.RUG,
                Lucide.Grid2x2,
                "Rug"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.BANNER,
                Lucide.Flag,
                "Banner"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.DECORATIVE,
                Lucide.Star,
                "Decorative"
            )
        )


        null -> emptyList() // Handle the null case explicitly
        BuildingMaterialSubCategory.WOOD -> emptyList()
        BuildingMaterialSubCategory.CORE_WOOD -> emptyList()
        BuildingMaterialSubCategory.RESOURCE -> emptyList()
        BuildingMaterialSubCategory.TRANSPORT -> listOf(
            BuildingMaterialChip(
                BuildingMaterialSubType.MISC,
                Lucide.Box,
                "Misc"
            ),
            BuildingMaterialChip(
                BuildingMaterialSubType.BOAT,
                Lucide.Sailboat,
                "Boat"
            )
        )

        BuildingMaterialSubCategory.DEFENSE -> emptyList()
        BuildingMaterialSubCategory.SIEGE -> emptyList()
        BuildingMaterialSubCategory.DECORATIVE -> emptyList()
        BuildingMaterialSubCategory.ROOF -> emptyList()

    }
}