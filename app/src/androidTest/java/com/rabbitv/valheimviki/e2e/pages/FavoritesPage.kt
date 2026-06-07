package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Favorites screen.
 *  - [E2ETestTags.FAVORITE_GRID_SCAFFOLD] — scaffold root
 *  - [E2ETestTags.favoriteItem] — per-card
 *  - [E2ETestTags.NAV_FAVORITES] — entry point (bookmark IconButton in MainTopBar)
 */
class FavoritesPage(private val compose: ComposeTestRule) {

	fun open(): FavoritesPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.NAV_FAVORITES))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.NAV_FAVORITES).performClick()
		compose.waitForIdle()
	}

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.FAVORITE_GRID_SCAFFOLD))
				.fetchSemanticsNodes().size == 1
		}
		compose.onNodeWithTag(E2ETestTags.FAVORITE_GRID_SCAFFOLD).assertIsDisplayed()
	}

	fun count(): Int =
		compose.onAllNodes(hasTestTagStartingWith(E2ETestTags.FAVORITE_ITEM_PREFIX))
			.fetchSemanticsNodes().size

	fun assertCount(expected: Int, message: String): FavoritesPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) { count() == expected }
		assertEquals(message, expected, count())
	}

	fun containsFavorite(id: String): Boolean =
		compose.onAllNodes(hasTestTag(E2ETestTags.favoriteItem(id)))
			.fetchSemanticsNodes().isNotEmpty()

	fun assertContainsFavorite(id: String, message: String): FavoritesPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) { containsFavorite(id) }
		assertTrue(message, containsFavorite(id))
	}

	fun assertFavoriteLoaded(id: String, name: String): FavoritesPage = apply {
		assertContainsFavorite(id, "Favorite $id missing")
		compose.waitForText(name, substring = false)
		compose.waitForImageLoaded(E2ETestTags.favoriteItemImage(id))
	}

	fun openFavorite(id: String): FavoritesPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.favoriteItem(id)))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.favoriteItem(id)).performClick()
		compose.waitForIdle()
	}

	private fun hasTestTagStartingWith(prefix: String): SemanticsMatcher =
		SemanticsMatcher("TestTag starts with '$prefix'") { node ->
			node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
		}
}
