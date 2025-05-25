package com.rabbitv.valheimviki.presentation.components.grid.grid_category

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

interface GridCategoryOption<T> {
    val image: Int
    val icon : ImageVector
    val label: String
    val value: T
}