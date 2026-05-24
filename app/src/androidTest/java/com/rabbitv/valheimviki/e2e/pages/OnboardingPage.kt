package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

/**
 * Page object for the 3-page welcome carousel.
 *
 * UI surface used:
 *  - testTag("WelcomeScreen") — root column
 *  - testTag("OnboardingNextButton") — single elevated button that advances pages / finishes
 */
class OnboardingPage(private val compose: ComposeTestRule) {

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = 10_000) {
			compose.onAllNodes(hasTestTag("WelcomeScreen")).fetchSemanticsNodes().size == 1
		}
		compose.onNodeWithTag("WelcomeScreen").assertIsDisplayed()
	}

	/** Click "Next" 3 times to traverse pages 1→2→3 and trigger the final navigation. */
	fun completeAll(): OnboardingPage = apply {
		repeat(3) {
			compose.onNodeWithTag("OnboardingNextButton").performClick()
			compose.waitForIdle()
		}
	}
}
