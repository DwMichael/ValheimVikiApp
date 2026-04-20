package com.rabbitv.valheimviki.ui.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

/**
 * Layout metrics derived from the current window size class.
 *
 * Use these values for adaptive grid columns, list columns, paddings, etc.
 * For text sizing use BasicText with TextAutoSize.StepBased — it adapts
 * automatically to the available space without per-breakpoint overrides.
 *
 * Based on:
 *  - https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes
 *  - https://developer.android.com/develop/ui/compose/layouts/adaptive/support-different-display-sizes
 *  - https://developer.android.com/develop/ui/compose/layouts/adaptive/adaptive-dos-and-donts
 */
@Immutable
data class AdaptiveLayoutInfo(
    val gridColumns: Int,
    val listColumns: Int,
    val categoryGridColumns: Int,
    val gridItemHeight: Dp,
    val listItemHeight: Dp,
    val listImageWidth: Dp,
    val contentPadding: Dp,
    val isExpandedWidth: Boolean,
    val isMediumWidth: Boolean,
)

val LocalAdaptiveLayoutInfo = staticCompositionLocalOf<AdaptiveLayoutInfo> {
    error("No AdaptiveLayoutInfo provided — wrap your content with ProvideAdaptiveLayout")
}

/**
 * Computes [AdaptiveLayoutInfo] from [currentWindowAdaptiveInfo].
 *
 * Uses [WindowSizeClass.isWidthAtLeastBreakpoint] (the non-deprecated API)
 * with the standard Material breakpoints:
 *  - Expanded: 840dp
 *  - Medium:   600dp
 */
@Composable
fun rememberAdaptiveLayoutInfo(): AdaptiveLayoutInfo {
    val windowInfo = currentWindowAdaptiveInfo()
    val sizeClass = windowInfo.windowSizeClass

    val isExpanded = sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val isMedium = !isExpanded && sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    return remember(isExpanded, isMedium) {
        AdaptiveLayoutInfo(
            gridColumns = when {
                isExpanded -> 4
                isMedium -> 3
                else -> 2
            },
            listColumns = if (isExpanded) 2 else 1,
            categoryGridColumns = when {
                isExpanded -> 4
                isMedium -> 3
                else -> 2
            },
            gridItemHeight = when {
                isExpanded -> 220.dp
                isMedium -> 200.dp
                else -> 200.dp
            },
            listItemHeight = if (isExpanded) 90.dp else 80.dp,
            listImageWidth = when {
                isExpanded -> 140.dp
                isMedium -> 130.dp
                else -> 120.dp
            },
            contentPadding = when {
                isExpanded -> 16.dp
                isMedium -> 12.dp
                else -> 10.dp
            },
            isExpandedWidth = isExpanded,
            isMediumWidth = isMedium,
        )
    }
}

/**
 * Provides [AdaptiveLayoutInfo] to the composition tree.
 * Place this at the top of your app, wrapping your main content.
 */
@Composable
fun ProvideAdaptiveLayout(content: @Composable () -> Unit) {
    val layoutInfo = rememberAdaptiveLayoutInfo()
    CompositionLocalProvider(
        LocalAdaptiveLayoutInfo provides layoutInfo,
    ) {
        content()
    }
}
