package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

/**
 * Biome detail screen.
 *  - [E2ETestTags.BIOME_DETAIL_SCREEN] — root
 *  - [E2ETestTags.DETAIL_BACK] — visible app back button
 *  - [E2ETestTags.FAVORITE_TOGGLE] — FilledIconToggleButton (FavoriteButton)
 */
class BiomeDetailPage(private val compose: ComposeTestRule) {

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.BIOME_DETAIL_SCREEN))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.BIOME_DETAIL_SCREEN).assertIsDisplayed()
	}

	fun tapFavorite(expectedChecked: Boolean): BiomeDetailPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.FAVORITE_TOGGLE))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.FAVORITE_TOGGLE).performClick()
		compose.waitForIdle()
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(favoriteToggleState(expectedChecked))
				.fetchSemanticsNodes().isNotEmpty()
		}
	}

	fun assertContent(id: String, title: String, descriptionFragment: String): BiomeDetailPage = apply {
		assertVisible()
		compose.waitForText(title, substring = false)
		compose.waitForText(descriptionFragment, substring = true)
		compose.waitForImageLoaded(E2ETestTags.detailImage(id))
	}

	fun goBackToBiomes(): BiomeDetailPage = apply {
		tapBack()
		compose.waitUntil(timeoutMillis = E2ETimeouts.VERY_LONG_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.BIOME_SURFACE))
				.fetchSemanticsNodes().isNotEmpty()
		}
	}

	fun goBackToFavorites(): BiomeDetailPage = apply {
		tapBack()
		compose.waitUntil(timeoutMillis = E2ETimeouts.VERY_LONG_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.FAVORITE_GRID_SCAFFOLD))
				.fetchSemanticsNodes().isNotEmpty()
		}
	}

	private fun tapBack() {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.DETAIL_BACK))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.DETAIL_BACK).performClick()
		compose.waitForIdle()
	}

	private fun favoriteToggleState(expectedChecked: Boolean): SemanticsMatcher {
		val expectedState = if (expectedChecked) ToggleableState.On else ToggleableState.Off
		return SemanticsMatcher("Favorite toggle is $expectedState") { node ->
			node.config.getOrNull(SemanticsProperties.TestTag) == E2ETestTags.FAVORITE_TOGGLE &&
					node.config.getOrNull(SemanticsProperties.ToggleableState) == expectedState
		}
	}
}
