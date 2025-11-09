package com.rabbitv.valheimviki.presentation.components.chip

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.ui.theme.AppStyleDefaults
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun <T> CustomElevatedFilterChip(
	index: Int,
	selectedChipIndex: Int?,
	onSelectedChange: (index: Int, option: T?) -> Unit,
	label: String,
	icon: ImageVector,
	option: T,
) {

	ElevatedFilterChip(
		selected = index == selectedChipIndex,
		onClick = { onSelectedChange(index, option) },
		label = { Text(label) },
		colors = AppStyleDefaults.yellowDTSelectableChipColors(),
		leadingIcon = if (index == selectedChipIndex) {
			{
				Icon(
					Lucide.Check,
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
			option = WeaponSubType.CROSSBOW,
		)
	}

}