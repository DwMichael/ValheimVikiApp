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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.CategoryGrid
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialCategoryScreen(
    onGridCategoryClick: () -> Unit,
    modifier: Modifier, paddingValues: PaddingValues,
    viewModel: MaterialListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyGridState = rememberLazyGridState()

    val materialCategories = MaterialSegmentOption.entries


    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("MaterialListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(BODY_CONTENT_PADDING.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                when (val state = uiState) {
                    is UiCategoryChipState.Loading -> {
                        Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
                        ShimmerGridEffect()
                    }

                    is UiCategoryChipState.Error -> EmptyScreen(errorMessage = state.message)
                    is UiCategoryChipState.Success ->
                        CategoryGrid<MaterialSubCategory>(
                            modifier = modifier,
                            items = materialCategories,
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


