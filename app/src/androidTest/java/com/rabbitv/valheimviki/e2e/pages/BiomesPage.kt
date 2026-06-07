package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

/**
 * Biome grid landing screen.
 *  - [E2ETestTags.BIOME_SURFACE] — root surface
 *  - [E2ETestTags.gridItem] — per-cell, shared across all grids via AnimatedGridItem
 */
class BiomesPage(private val compose: ComposeTestRule) {

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.VERY_LONG_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.BIOME_SURFACE))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.BIOME_SURFACE).assertIsDisplayed()
	}

	fun openBiome(id: String): BiomesPage = apply {
		val tag = E2ETestTags.gridItem(id)
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(tag)).fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(tag).performClick()
		compose.waitForIdle()
	}

	fun assertBiomeLoaded(id: String, name: String): BiomesPage = apply {
		compose.waitForNodeWithTag(E2ETestTags.gridItem(id), E2ETimeouts.VERY_LONG_MS)
		compose.waitForText(name, substring = false)
		compose.waitForImageLoaded(E2ETestTags.gridItemImage(id))
	}
}
