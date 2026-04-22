package com.rabbitv.valheimviki.presentation.components.segmented

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Apple
import com.composables.icons.lucide.CookingPot
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.AppStyleDefaults
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun <T> SegmentedButtonSingleSelect(
	modifier: Modifier = Modifier,
	options: List<SegmentedOption<T>>,
	selectedOption: T,
	onOptionSelected: (T) -> Unit,
	icons: List<ImageVector>? = null,
) {

	SingleChoiceSegmentedButtonRow(
		modifier = modifier.fillMaxWidth()
	) {
		options.forEachIndexed { index, option ->
			SegmentedButton(
				modifier = Modifier.height(40.dp),
				shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
				onClick = { onOptionSelected(option.value) },
				selected = option.value == selectedOption,
				colors = AppStyleDefaults.yellowDTSegmentedButtonColors(),
				icon = {
					if (!icons.isNullOrEmpty()) {
						Icon(
							imageVector = icons[index],
							contentDescription = stringResource(R.string.cd_segment_icon)
						)
					}
				}
			) {
				BasicText(
					text  = stringResource(option.labelRes).uppercase(),
					style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold,color = PrimaryWhite),
					autoSize = TextAutoSize.StepBased(),
				)
			}
		}
	}
}

enum class SegmentObject(
	override val labelRes: Int,
	override val value: String
) : SegmentedOption<String> {
	OBJECT1(R.string.segment_button_1, "CATEGORY"),
	OBJECT2(R.string.segment_button_2, "CATEGORY"),
	OBJECT3(R.string.segment_button_3, "CATEGORY")
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

@Preview("SegmentedButtonSingleSelectPreview")
@Composable
fun PreviewSegmentedButtonSingleSelectWithIcons() {
	ValheimVikiAppTheme {
		SegmentedButtonSingleSelect(
			options = SegmentObject.entries,
			selectedOption = SegmentObject.OBJECT1.toString(),
			onOptionSelected = { },
			icons = listOf(
				Lucide.CookingPot,
				Lucide.Apple,
				Lucide.Apple,
			)
		)
	}
}

