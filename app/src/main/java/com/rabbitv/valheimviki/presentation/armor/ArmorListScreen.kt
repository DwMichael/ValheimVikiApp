package com.rabbitv.valheimviki.presentation.armor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Blend
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.ErrorType
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.CustomElevatedFilterChip
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


class ArmorChip(
    override val option: ArmorSubCategory,
    override val icon: ImageVector,
    override val label: String
) : ChipData<ArmorSubCategory>

@Composable
fun ArmorListScreen(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: ArmorListViewModel = hiltViewModel()
) {
    val weaponListUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onChipSelected = { chip: ArmorSubCategory? -> viewModel.onChipSelected(chip) }
    ArmorListStateRenderer(
        armorListUiState = weaponListUiState,
        onChipSelected = onChipSelected,
        paddingValues = paddingValues,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorListStateRenderer(
    armorListUiState: ArmorListUiState,
    onChipSelected: (ArmorSubCategory?) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier,
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("WeaponListSurface")
            .fillMaxSize()
            .padding(paddingValues)
            .then(modifier)
    ) {

        when {
            armorListUiState.isLoading || (armorListUiState.armorList.isEmpty() && armorListUiState.isConnection) -> {
                ShimmerListEffect()
            }

            (armorListUiState.error != null || !armorListUiState.isConnection) && armorListUiState.armorList.isEmpty() -> {
                Box(
                    modifier = Modifier.testTag("EmptyScreenWeaponList"),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("EmptyScreenWeaponList")
                    ) {
                        EmptyScreen(
                            errorMessage = armorListUiState.error
                                ?: "Connect to internet to fetch data",
                            errorType = ErrorType.INTERNET_CONNECTION
                        )
                    }
                }
            }

            else -> {
                ArmorListDisplay(
                    armorListUiState = armorListUiState,
                    onChipSelected = onChipSelected,
                )
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun ArmorListDisplay(
    armorListUiState: ArmorListUiState,
    onChipSelected: (ArmorSubCategory?) -> Unit,
) {

    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )
    val selectedDifferentCategory = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(armorListUiState.selectedChip) {
        if (selectedDifferentCategory.value) {
            coroutineScope.launch {
                lazyListState.scrollToItem(0)
                scrollPosition.intValue = 0
            }
            selectedDifferentCategory.value = false
        }
    }

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        SearchFilterBar(
            chips = getChipsForCategory(),
            selectedOption = armorListUiState.selectedChip,
            onSelectedChange = { _, subCategory ->
                if (armorListUiState.selectedChip == subCategory) {
                    onChipSelected(null)
                } else {
                    onChipSelected(subCategory)
                }
            },
            modifier = Modifier,
        )
        Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
        ListContent(
            items = armorListUiState.armorList,
            clickToNavigate = { s, i -> {} },
            lazyListState = lazyListState,
            subCategoryNumber = 0,
            imageScale = ContentScale.Fit,
            horizontalPadding = 0.dp
        )
    }
}


@Composable
private fun getChipsForCategory(): List<ArmorChip> {
    return listOf(
        ArmorChip(
            ArmorSubCategory.CAPE,
            ImageVector.vectorResource(id = R.drawable.cape),
            "Capes"
        ),
        ArmorChip(
            ArmorSubCategory.CHEST_ARMOR,
            ImageVector.vectorResource(id = R.drawable.chest_armor),
            "Chests Armor"
        ),
        ArmorChip(
            ArmorSubCategory.LEG_ARMOR,
            ImageVector.vectorResource(id = R.drawable.leg_armor),
            "Legs Armor"
        ),
        ArmorChip(
            ArmorSubCategory.ACCESSORIES,
            Lucide.Blend,
            "Accessories"
        ),
        ArmorChip(
            ArmorSubCategory.HELMET,
            ImageVector.vectorResource(id = R.drawable.helmet),
            "Helmet"
        ),
    )
}


@Preview("SingleChoiceChipPreview")
@Composable
fun PreviewSingleChoiceChip() {
    ValheimVikiAppTheme {
        SearchFilterBar(
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
            selectedOption = ArmorSubCategory.CAPE,
            chips = getChipsForCategory()
        )
    }
}


@Preview("CustomElevatedFilterChipSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipSelected() {
    ValheimVikiAppTheme {
        CustomElevatedFilterChip(

            index = 0,
            selectedChipIndex = 0,
            onSelectedChange = { i, s -> {} },
            label = "Axes",
            icon = Lucide.Axe,
            option = ArmorSubCategory.CAPE,
        )
    }

}

@Preview("CustomElevatedFilterChipNotSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipNotSelected() {
    ValheimVikiAppTheme {
        CustomElevatedFilterChip(
            index = 1,
            selectedChipIndex = 0,
            onSelectedChange = { i, s -> {} },
            label = "Axes",
            icon = Lucide.Axe,
            option = ArmorSubCategory.CAPE,
        )
    }

}


@Preview(name = "WeaponListStateRendererPreview")
@Composable
fun PreviewWeaponListStateRenderer() {
    ValheimVikiAppTheme {
        ArmorListStateRenderer(
            armorListUiState = ArmorListUiState(
                armorList = FakeData.fakeArmorList(),
                isConnection = true,
                isLoading = false,
                error = null,
            ),
            paddingValues = PaddingValues(),
            modifier = Modifier,
            onChipSelected = {},
        )
    }
}




