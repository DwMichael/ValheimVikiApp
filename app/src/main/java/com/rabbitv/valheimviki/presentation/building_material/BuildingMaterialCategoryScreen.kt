package com.rabbitv.valheimviki.presentation.building_material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Armchair
import com.composables.icons.lucide.Crosshair
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lamp
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Package
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Truck
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialSegmentOption
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.CategoryGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingMaterialCategoryScreen(
    onGridCategoryClick: () -> Unit,
    modifier: Modifier, paddingValues: PaddingValues,
    viewModel: BuildingMaterialListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyGridState = rememberLazyGridState()

    val buildingMaterialCategories = BuildingMaterialSegmentOption.entries.sortedBy { it.label }


    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("MaterialListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if ((uiState.error != null || !uiState.isConnection) && uiState.buildingMaterialList.isEmpty()) {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("EmptyScreenMaterialList"),
                errorMessage = uiState.error ?: "Please connect to the internet to fetch data."
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading && (uiState.buildingMaterialList.isEmpty() && uiState.isConnection)) {
                        Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                        ShimmerGridEffect()
                    } else {
                        CategoryGrid<BuildingMaterialSubCategory>(
                            modifier = modifier,
                            items = buildingMaterialCategories,
                            onItemClick = { categorySegmentOption ->
                                viewModel.onCategorySelected(categorySegmentOption)
                                onGridCategoryClick()
                            },
                            numbersOfColumns = 2,
                            height = 200.dp,
                            lazyGridState = lazyGridState
                        )

                    }
                }
            }
        }
    }
}


