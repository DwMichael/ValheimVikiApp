package com.rabbitv.valheimviki.presentation.weapons

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
import com.composables.icons.lucide.Bomb
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.WandSparkles
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerGridEffect
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponListUiState
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponSegmentOption
import com.rabbitv.valheimviki.presentation.weapons.viewmodel.WeaponListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


data class WeaponChip(
    override val option: WeaponSubType,
    override val icon: ImageVector,
    override val label: String
) : ChipData<WeaponSubType>

@Composable
fun WeaponListScreen(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: WeaponListViewModel = hiltViewModel()
) {
    val weaponListUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onCategorySelected = { cat: WeaponSubCategory -> viewModel.onCategorySelected(cat) }
    val onChipSelected = { chip: WeaponSubType? -> viewModel.onChipSelected(chip) }
    WeaponListStateRenderer(
        weaponListUiState = weaponListUiState,
        onCategorySelected = onCategorySelected,
        onChipSelected = onChipSelected,
        paddingValues = paddingValues,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponListStateRenderer(
    weaponListUiState: WeaponListUiState,
    onCategorySelected: (WeaponSubCategory) -> Unit,
    onChipSelected: (WeaponSubType?) -> Unit,
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
            weaponListUiState.isLoading || (weaponListUiState.weaponList.isEmpty() && weaponListUiState.isConnection) -> {
                ShimmerListEffect()
            }

            (weaponListUiState.error != null || !weaponListUiState.isConnection) && weaponListUiState.weaponList.isEmpty() -> {
                Box(
                    modifier = Modifier.testTag("EmptyScreenWeaponList"),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("EmptyScreenWeaponList")
                    ) {
                        EmptyScreen(
                            modifier = Modifier.fillMaxSize(),
                            errorMessage = weaponListUiState.error
                                ?: "Connect to internet to fetch data"
                        )
                    }
                }
            }

            else -> {
                WeaponListDisplay(
                    weaponListUiState = weaponListUiState,
                    onCategorySelected = onCategorySelected,
                    onChipSelected = onChipSelected,
                )
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun WeaponListDisplay(
    weaponListUiState: WeaponListUiState,
    onCategorySelected: (WeaponSubCategory) -> Unit,
    onChipSelected: (WeaponSubType?) -> Unit,
) {
    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )
    val showAll = remember { mutableStateOf(false) }
    val selectedDifferentCategory = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val selectedChipIndex = getWeaponCategoryIndex(category = weaponListUiState.selectedCategory)

    LaunchedEffect(weaponListUiState.selectedCategory) {
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
        SegmentedButtonSingleSelect(
            options = WeaponSegmentOption.entries,
            selectedOption = weaponListUiState.selectedCategory,
            onOptionSelected = { index ->
                selectedDifferentCategory.value = true
                onCategorySelected(index)
                onChipSelected(null)
            }
        )
        Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))

        SearchFilterBar(
            chips = getChipsForCategory(weaponListUiState.selectedCategory),
            selectedOption = weaponListUiState.selectedChip,
            onSelectedChange = { _, subType ->
                if (weaponListUiState.selectedChip == subType) {
                    onChipSelected(null)
                } else {
                    onChipSelected(subType)
                }
            },
            modifier = Modifier
        )


        Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
        ListContent(
            items = weaponListUiState.weaponList,
            clickToNavigate = { s, i -> {} },
            lazyListState = lazyListState,
            subCategoryNumber = selectedChipIndex,
            imageScale = ContentScale.Fit,
            horizontalPadding = 0.dp
        )
    }
}


@Composable
private fun getChipsForCategory(category: WeaponSubCategory): List<WeaponChip> {
    return when (category) {
        WeaponSubCategory.MELEE_WEAPON -> listOf(
            WeaponChip(WeaponSubType.AXE, Lucide.Axe, "Axes"),
            WeaponChip(
                WeaponSubType.CLUB,
                ImageVector.vectorResource(id = R.drawable.club),
                "Clubs"
            ),
            WeaponChip(
                WeaponSubType.SWORD,
                ImageVector.vectorResource(id = R.drawable.sword),
                "Swords"
            ),
            WeaponChip(
                WeaponSubType.SPEAR,
                ImageVector.vectorResource(id = R.drawable.spear),
                "Spears"
            ),
            WeaponChip(
                WeaponSubType.POLEARM,
                ImageVector.vectorResource(id = R.drawable.polearm),
                "Polearms"
            ), // Fixed typo
            WeaponChip(
                WeaponSubType.KNIFES,
                ImageVector.vectorResource(id = R.drawable.knife),
                "Knives"
            ),
            WeaponChip(WeaponSubType.FISTS, Lucide.Grab, "Fists"),
            WeaponChip(WeaponSubType.SHIELD, Lucide.Shield, "Shields")
        )

        WeaponSubCategory.RANGED_WEAPON -> listOf(
            WeaponChip(
                WeaponSubType.BOW,
                ImageVector.vectorResource(id = R.drawable.bow_arrow),
                "Bows"
            ),
            WeaponChip(
                WeaponSubType.CROSSBOW,
                ImageVector.vectorResource(id = R.drawable.crossbow),
                "Crossbows"
            )
        )

        WeaponSubCategory.MAGIC_WEAPON -> listOf(
            WeaponChip(WeaponSubType.ELEMENTAL_MAGIC, Lucide.WandSparkles, "Elemental magic"),
            WeaponChip(WeaponSubType.BLOOD_MAGIC, Lucide.Wand, "Blood magic")
        )

        WeaponSubCategory.AMMO -> listOf(
            WeaponChip(
                WeaponSubType.ARROW,
                ImageVector.vectorResource(id = R.drawable.arrow),
                "Arrows"
            ),
            WeaponChip(
                WeaponSubType.BOLT,
                ImageVector.vectorResource(id = R.drawable.bolt),
                "Bolts"
            ), // Fixed label
            WeaponChip(
                WeaponSubType.MISSILE,
                ImageVector.vectorResource(id = R.drawable.missile),
                "Missiles"
            ),
            WeaponChip(WeaponSubType.BOMB, Lucide.Bomb, "Bombs")
        )
    }
}


@Preview("SingleChoiceChipMeleePreview")
@Composable
fun PreviewSingleChoiceChipMelee() {
    ValheimVikiAppTheme {
        SearchFilterBar(
            chips = getChipsForCategory(WeaponSubCategory.MELEE_WEAPON),
            selectedOption = WeaponSubType.SWORD,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
        )
    }
}

@Preview("SingleChoiceChipRangedPreview")
@Composable
fun PreviewSingleChoiceChipRanged() {


    ValheimVikiAppTheme {
        SearchFilterBar(
            chips = getChipsForCategory(WeaponSubCategory.RANGED_WEAPON),
            selectedOption = WeaponSubType.BOW,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
        )
    }
}

@Preview("SingleChoiceChipMagicPreview")
@Composable
fun PreviewSingleChoiceChipMagic() {

    ValheimVikiAppTheme {
        SearchFilterBar(
            chips = getChipsForCategory(WeaponSubCategory.MAGIC_WEAPON),
            selectedOption = WeaponSubType.ELEMENTAL_MAGIC,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
        )
    }
}

@Preview("SingleChoiceChipAmmoPreview")
@Composable
fun PreviewSingleChoiceChipAmmo() {

    ValheimVikiAppTheme {
        SearchFilterBar(
            selectedOption = WeaponSubType.BOMB,
            onSelectedChange = { i, s -> {} },
            chips = getChipsForCategory(WeaponSubCategory.AMMO),
            modifier = Modifier,
        )
    }
}


@Preview(name = "WeaponListStateRendererPreview")
@Composable
fun PreviewWeaponListStateRenderer() {
    ValheimVikiAppTheme {
        WeaponListStateRenderer(
            weaponListUiState = WeaponListUiState(
                weaponList = FakeData.fakeWeaponList,
                isConnection = true,
                isLoading = false,
                error = null,
            ),
            paddingValues = PaddingValues(),
            modifier = Modifier,
            onCategorySelected = {},
            onChipSelected = {},
        )
    }
}


private fun getWeaponCategoryIndex(category: WeaponSubCategory): Int {
    return when (category) {
        WeaponSubCategory.MELEE_WEAPON -> 0
        WeaponSubCategory.RANGED_WEAPON -> 1
        WeaponSubCategory.MAGIC_WEAPON -> 2
        WeaponSubCategory.AMMO -> 3
    }
}

private fun getWeaponCategory(index: Int): WeaponSubCategory {
    return when (index) {
        0 -> WeaponSubCategory.MELEE_WEAPON
        1 -> WeaponSubCategory.RANGED_WEAPON
        2 -> WeaponSubCategory.MAGIC_WEAPON
        else -> WeaponSubCategory.AMMO
    }
}