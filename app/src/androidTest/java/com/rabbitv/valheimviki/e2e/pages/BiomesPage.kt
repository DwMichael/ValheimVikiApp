package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

/**
 * Page object for the biome grid (post-onboarding landing).
 *
 * UI surface used:
 *  - testTag("BiomeSurface") — root surface of [BiomeGridScreen]
 *  - testTag("GridItem_<id>") — per-grid-cell (added to AnimatedGridItem, shared across all grids).
 */
class BiomesPage(private val compose: ComposeTestRule) {

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = 15_000) {
			compose.onAllNodes(hasTestTag("BiomeSurface")).fetchSemanticsNodes().size == 1
		}
		compose.onNodeWithTag("BiomeSurface").assertIsDisplayed()
	}

	/** Open the detail screen for a biome by id. */
	fun openBiome(id: String): BiomesPage = apply {
		compose.onNodeWithTag("GridItem_$id").performClick()
		compose.waitForIdle()
	}
}
