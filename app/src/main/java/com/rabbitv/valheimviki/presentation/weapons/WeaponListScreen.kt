package com.rabbitv.valheimviki.presentation.weapons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Bomb
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.SlidersHorizontal
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.WandSparkles
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.ui.theme.YellowDTContainerNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTIconColor
import com.rabbitv.valheimviki.ui.theme.YellowDTNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTSelected
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

enum class SegmentButtonOptions(val label: String) {
    MELEE("Melee"),
    RANGED("Ranged"),
    MAGIC("Magic"),
    AMMO("Ammo");
}

data class WeaponChip(
    val subType: WeaponSubType,
    val icon: ImageVector,
    val label: String                 // or @StringRes val label: Int
)

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

//        when{
//            weaponListUiState.isLoading ||  (weaponListUiState.weaponList.isEmpty() && weaponListUiState.isConnection) ->
//            {
//                ShimmerEffect()
//            }
//
//            (weaponListUiState.error != null || !weaponListUiState.isConnection) && weaponListUiState.weaponList.isEmpty() -> {
//                Box(
//                    modifier = Modifier.testTag("EmptyScreenWeaponList"),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .testTag("EmptyScreenWeaponList")
//                    ) {
//                        EmptyScreen(
//                            modifier = Modifier.fillMaxSize(),
//                            errorMessage = weaponListUiState.error
//                                ?: "Connect to internet to fetch data"
//                        )
//                    }
//                }
//            }
        WeaponListDisplay(
            weaponListUiState = weaponListUiState,
            onCategorySelected = onCategorySelected,
            onChipSelected = onChipSelected,
        )

//            else ->{
//
//            }

//        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun WeaponListDisplay(
    weaponListUiState: WeaponListUiState,
    onCategorySelected: (WeaponSubCategory) -> Unit,
    onChipSelected: (WeaponSubType?) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scrollPosition = remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.intValue
    )
    val selectedDifferentCategory = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val selectedChipIndex = getWeaponCategoryIndex(category = weaponListUiState.selectedCategory)

    LaunchedEffect(weaponListUiState.selectedCategory) {
        if (selectedDifferentCategory.value) {
            coroutineScope.launch {
                lazyListState.scrollToItem(0)
                scrollPosition.value = 0
            }
            selectedDifferentCategory.value = false
        }
    }

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        SegmentedButtonSingleSelect(
            selectedCategory = weaponListUiState.selectedCategory,
            onCategoryClick = { index ->
                selectedDifferentCategory.value = true
                onCategorySelected(getWeaponCategory(index))
                onChipSelected(null)
            }
        )
        Spacer(
            Modifier.padding(
                top = 5.dp,
                start = BODY_CONTENT_PADDING.dp,
                bottom = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp
            )
        )
        IconButton(
            onClick = {
                visible = !visible
            },
            modifier = Modifier
                .align(Alignment.End)
                .size(64.dp, 36.dp)
                .clip(RoundedCornerShape(1.dp)),
            interactionSource = interactionSource,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isPressed) YellowDT else YellowDTContainerNotSelected,
                contentColor = if (isPressed) YellowDTIconColor else YellowDTSelected,
                disabledContainerColor = YellowDT,
                disabledContentColor = YellowDTIconColor
            )
        ) {
            Icon(
                Lucide.SlidersHorizontal,
                contentDescription = "Slider Icon",
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
        AnimatedVisibility(visible) {
            SingleChoiceChipGroup(
                selectedCategory = weaponListUiState.selectedCategory,
                selectedSubType = weaponListUiState.selectedChip,
                onSelectedChange = { _, subType ->
                    if (weaponListUiState.selectedChip == subType) {
                        onChipSelected(null)
                    } else {
                        onChipSelected(subType)
                    }
                },
                modifier = Modifier,
            )
        }
        Spacer(
            Modifier.padding(
                start = BODY_CONTENT_PADDING.dp,
                top = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp
            )
        )
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
fun SingleChoiceChipGroup(
    selectedCategory: WeaponSubCategory,
    selectedSubType: WeaponSubType?,
    onSelectedChange: (index: Int, subType: WeaponSubType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val chips = getChipsForCategory(selectedCategory)


    val selectedIndex = if (selectedSubType == null) {
        null
    } else {
        chips.indexOfFirst { it.subType == selectedSubType }
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.let { items ->
            items.forEachIndexed { index, weaponChip ->
                CustomElevatedFilterChip(
                    index = index,
                    selectedChipIndex = selectedIndex,
                    onSelectedChange = onSelectedChange,
                    label = weaponChip.label,
                    icon = weaponChip.icon,
                    subType = weaponChip.subType
                )
            }
        }
    }
}

@Composable
fun CustomElevatedFilterChip(
    index: Int,
    selectedChipIndex: Int?,
    onSelectedChange: (index: Int, subType: WeaponSubType?) -> Unit,
    label: String,
    icon: ImageVector,
    subType: WeaponSubType,

    ) {
    val selectableChipColors = SelectableChipColors(
        containerColor = YellowDTContainerNotSelected,
        labelColor = YellowDTNotSelected,
        leadingIconColor = YellowDTIconColor,
        trailingIconColor = YellowDTIconColor,
        disabledContainerColor = YellowDTContainerNotSelected,
        disabledLabelColor = YellowDTNotSelected,
        disabledLeadingIconColor = YellowDTIconColor,
        disabledTrailingIconColor = YellowDTIconColor,
        selectedContainerColor = YellowDT,
        disabledSelectedContainerColor = YellowDTSelected,
        selectedLabelColor = YellowDTSelected,
        selectedLeadingIconColor = YellowDTSelected,
        selectedTrailingIconColor = YellowDTSelected
    )
    ElevatedFilterChip(
        selected = index == selectedChipIndex,
        onClick = { onSelectedChange(index, subType) },
        label = { Text(label) },
        colors = selectableChipColors,
        leadingIcon = if (index == selectedChipIndex) {
            {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        }
    )
}


@Composable
fun SegmentedButtonSingleSelect(
    selectedCategory: WeaponSubCategory,
    onCategoryClick: (index: Int) -> Unit,
) {

    val options: List<SegmentButtonOptions> = SegmentButtonOptions.entries

    val segmentColors = SegmentedButtonDefaults.colors(
        // selected (ON) state
        activeContainerColor = YellowDT,
        activeContentColor = YellowDTSelected,

        // un-selected (OFF) state
        inactiveContainerColor = YellowDTContainerNotSelected,
        inactiveContentColor = YellowDTNotSelected,

        // disabled states – optional but nice
        disabledActiveContainerColor = YellowDT.copy(alpha = 0.38f),
        disabledActiveContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        disabledInactiveContainerColor = Color.Transparent,
        disabledInactiveContentColor = YellowDT.copy(alpha = 0.38f),
        activeBorderColor = YellowDTBorder,
        inactiveBorderColor = YellowDTBorder,
        disabledActiveBorderColor = YellowDTBorder,
        disabledInactiveBorderColor = YellowDTBorder,
    )
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onCategoryClick(index) },
                selected = index == getWeaponCategoryIndex(selectedCategory),
                colors = segmentColors,
                icon = {}
            ) {
                Text(
                    label.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
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
        SingleChoiceChipGroup(
            selectedCategory = WeaponSubCategory.MELEE_WEAPON,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
            selectedSubType = WeaponSubType.SWORD,
        )
    }
}

@Preview("SingleChoiceChipRangedPreview")
@Composable
fun PreviewSingleChoiceChipRanged() {


    ValheimVikiAppTheme {
        SingleChoiceChipGroup(
            selectedCategory = WeaponSubCategory.RANGED_WEAPON,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
            selectedSubType = WeaponSubType.BOW,

            )
    }
}

@Preview("SingleChoiceChipMagicPreview")
@Composable
fun PreviewSingleChoiceChipMagic() {

    ValheimVikiAppTheme {
        SingleChoiceChipGroup(
            selectedCategory = WeaponSubCategory.MAGIC_WEAPON,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
            selectedSubType = WeaponSubType.ELEMENTAL_MAGIC,
        )
    }
}

@Preview("SingleChoiceChipAmmoPreview")
@Composable
fun PreviewSingleChoiceChipAmmo() {

    ValheimVikiAppTheme {
        SingleChoiceChipGroup(
            selectedCategory = WeaponSubCategory.AMMO,
            onSelectedChange = { i, s -> {} },
            modifier = Modifier,
            selectedSubType = WeaponSubType.BOMB,
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
            subType = WeaponSubType.CROSSBOW,
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
            subType = WeaponSubType.CROSSBOW
        )
    }

}

@Preview("SegmentedButtonSingleSelectPreview")
@Composable
fun PreviewSegmentedButtonSingleSelect() {
    ValheimVikiAppTheme {
        SegmentedButtonSingleSelect(
            selectedCategory = WeaponSubCategory.MELEE_WEAPON,
            onCategoryClick = { }
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