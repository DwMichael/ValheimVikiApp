package com.rabbitv.valheimviki.presentation.components.chip

import androidx.compose.ui.graphics.vector.ImageVector

interface ChipData<T> {
    val option: T
    val icon: ImageVector
    val label: String
}


