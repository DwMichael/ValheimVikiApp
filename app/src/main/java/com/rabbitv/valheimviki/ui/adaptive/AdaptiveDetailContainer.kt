package com.rabbitv.valheimviki.ui.adaptive

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Modifier for the scrollable Column inside detail screens.
 * Content fills the entire available width on all screen sizes.
 */
@Composable
fun Modifier.adaptiveDetailWidth(): Modifier {
    return this.fillMaxSize()
}
