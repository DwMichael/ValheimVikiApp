package com.rabbitv.valheimviki.presentation.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType

import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun <T> SingleChoiceChipFlowRow(
    selectedOption: T?,
    onSelectedChange: (index: Int, option: T?) -> Unit,
    chips: List<ChipData<T>>,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = if (selectedOption == null) {
        null
    } else {
        chips.indexOfFirst { it.option == selectedOption }
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
                    option = weaponChip.option
                )
            }
        }
    }
}


