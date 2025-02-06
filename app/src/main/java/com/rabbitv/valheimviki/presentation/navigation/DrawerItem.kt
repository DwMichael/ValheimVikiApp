package com.rabbitv.valheimviki.presentation.navigation

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val iconPainter: Painter? = null,
    val icon: ImageVector? = null,
    val label: String,
    val contentDescription: String,
    val route: String = label
)
