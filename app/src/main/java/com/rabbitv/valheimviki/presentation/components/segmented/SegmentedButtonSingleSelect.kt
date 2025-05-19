package com.rabbitv.valheimviki.presentation.components.segmented

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rabbitv.valheimviki.ui.theme.AppStyleDefaults
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun <T> SegmentedButtonSingleSelect(
    options: List<SegmentedOption<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    icons: List<ImageVector>? = null
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
                icon = {
                    if (!icons.isNullOrEmpty()) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = "Segment Icon"
                        )
                    }
                }
            ) {
                Text(
                    option.label.toString().uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

enum class SegmentObject(
    override val label: String,
    override val value: String
) : SegmentedOption<String> {
    OBJECT1("BUTTON 1", "CATEGORY"),
    OBJECT2("BUTTON 2", "CATEGORY"),
    OBJECT3("BUTTON 3", "CATEGORY")
}


@Preview("SegmentedButtonSingleSelectPreview")
@Composable
fun PreviewSegmentedButtonSingleSelect() {
    ValheimVikiAppTheme {
        SegmentedButtonSingleSelect(
            options = SegmentObject.entries,
            selectedOption = SegmentObject.OBJECT1.toString(),
            onOptionSelected = { },
        )
    }
}

