package com.rabbitv.valheimviki.presentation.components.segmented

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.ui.theme.AppStyleDefaults
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun <T> SegmentedButtonSingleSelect(
    options: List<SegmentedOption<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
) {

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onOptionSelected(option.value) },
                selected = option.value == selectedOption,
                colors = AppStyleDefaults.yellowDTSegmentedButtonColors(),
                icon = {}
            ) {
                Text(
                    option.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
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