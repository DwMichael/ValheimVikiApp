package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

/**
 * Page object for the Favorites screen.
 *
 * UI surface used:
 *  - testTag("FavoriteGridScaffold") — scaffold root
 *  - testTag("FavoriteItem_<id>") — per-favorite-card (FavoriteGridItem)
 *  - testTag("nav_favorites") — entry point (MainAppBar bookmark IconButton)
 */
class FavoritesPage(private val compose: ComposeTestRule) {

	fun open(): FavoritesPage = apply {
		compose.onNodeWithTag("nav_favorites").performClick()
		compose.waitForIdle()
	}

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = 10_000) {
			compose.onAllNodes(hasTestTag("FavoriteGridScaffold")).fetchSemanticsNodes().size == 1
		}
		compose.onNodeWithTag("FavoriteGridScaffold").assertIsDisplayed()
	}

	/** Count of favorite cards rendered. */
	fun count(): Int =
		compose.onAllNodes(hasTestTagStartingWith("FavoriteItem_")).fetchSemanticsNodes().size

	fun containsFavorite(id: String): Boolean =
		compose.onAllNodes(hasTestTag("FavoriteItem_$id")).fetchSemanticsNodes().isNotEmpty()

	private fun hasTestTagStartingWith(prefix: String): SemanticsMatcher =
		SemanticsMatcher("TestTag starts with '$prefix'") { node ->
			node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
		}
}
