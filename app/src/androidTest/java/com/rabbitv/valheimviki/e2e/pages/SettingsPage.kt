package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

class SettingsPage(private val compose: ComposeTestRule) {

	fun openFromHighlightedSettings(): SettingsPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.NAV_SETTINGS))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.NAV_SETTINGS).performClick()
		assertVisible()
	}

	fun assertVisible(): SettingsPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.SETTINGS_LIST_SCAFFOLD))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.SETTINGS_LIST_SCAFFOLD).assertIsDisplayed()
	}

	fun completeTutorial(): SettingsPage = apply {
		repeat(SETTINGS_TUTORIAL_STEP_COUNT) {
			compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
				compose.onAllNodes(hasTestTag(E2ETestTags.SETTINGS_TUTORIAL_OVERLAY))
					.fetchSemanticsNodes().isNotEmpty()
			}
			compose.onNodeWithTag(E2ETestTags.SETTINGS_TUTORIAL_NEXT).performClick()
			compose.waitForIdle()
		}
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.SETTINGS_TUTORIAL_OVERLAY))
				.fetchSemanticsNodes().isEmpty()
		}
	}

	fun goBackToBiomes(): SettingsPage = apply {
		compose.onNodeWithTag(E2ETestTags.SIMPLE_TOP_BAR_BACK).performClick()
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.BIOME_SURFACE))
				.fetchSemanticsNodes().isNotEmpty()
		}
	}

	private companion object {
		const val SETTINGS_TUTORIAL_STEP_COUNT = 2
	}
}
