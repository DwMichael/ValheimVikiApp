package com.rabbitv.valheimviki.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val icon: ImageVector,
    val label: String,
    val contentDescription: String,
    val route: String = label
)
