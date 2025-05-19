package com.rabbitv.valheimviki.presentation.components.chip

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.SlidersHorizontal
import com.rabbitv.valheimviki.ui.theme.AppStyleDefaults
import com.rabbitv.valheimviki.ui.theme.PrimaryGrey

@Composable
fun <T> SearchFilterBar(
    chips: List<ChipData<T>>,
    selectedOption: T?,
    onSelectedChange: (index: Int, option: T?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val selectedIndex = selectedOption?.let { opt ->
        chips.indexOfFirst { it.option == opt }
    }

    Surface(
        tonalElevation = 1.dp,
        color = Color.Transparent,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        if (!expanded) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (chips.size > 3) {
                    item {
                        StartRow(expanded) { expanded = true }
                    }
                }
                itemsIndexed(chips) { index, chip ->
                    CustomElevatedFilterChip(
                        index = index,
                        selectedChipIndex = selectedIndex,
                        onSelectedChange = onSelectedChange,
                        label = chip.label,
                        icon = chip.icon,
                        option = chip.option
                    )
                }
            }
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (chips.size > 3) {
                    StartRow(expanded) { expanded = false }
                }

                chips.forEachIndexed { index, chip ->
                    CustomElevatedFilterChip(
                        index = index,
                        selectedChipIndex = selectedIndex,
                        onSelectedChange = onSelectedChange,
                        label = chip.label,
                        icon = chip.icon,
                        option = chip.option
                    )
                }
            }
        }

    }
}

@Composable
private fun StartRow(
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            border = null,
            selected = expanded,
            onClick = onToggle,
            label = { Text("Show all") },
            leadingIcon = {
                Icon(
                    imageVector = Lucide.SlidersHorizontal,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
            colors = AppStyleDefaults.yellowDTSelectableChipColors()
        )

        VerticalDivider(
            modifier = Modifier
                .height(FilterChipDefaults.Height)
                .padding(horizontal = 2.dp),
            thickness = 1.dp,
            color = PrimaryGrey
        )
    }
}
