package com.rabbitv.valheimviki.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppStyleDefaults {

    @Composable
    fun yellowDTSegmentedButtonColors(): SegmentedButtonColors =
        SegmentedButtonDefaults.colors(
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

    @Composable
    fun yellowDTSelectableChipColors(): SelectableChipColors =
        SelectableChipColors(
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
}