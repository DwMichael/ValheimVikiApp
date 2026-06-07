package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

/**
 * 3-page welcome carousel.
 *  - [E2ETestTags.WELCOME_SCREEN] — root
 *  - [E2ETestTags.ONBOARDING_NEXT] — single button: advances pages 1→2→3, then finishes
 */
class OnboardingPage(private val compose: ComposeTestRule) {

	fun assertVisible() = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.LONG_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.WELCOME_SCREEN))
				.fetchSemanticsNodes().size == 1
		}
		compose.onNodeWithTag(E2ETestTags.WELCOME_SCREEN).assertIsDisplayed()
	}

	/** Click Next 3 times to advance pages and trigger the final navigation away. */
	fun completeAll(): OnboardingPage = apply {
		repeat(WELCOME_PAGE_COUNT) {
			compose.waitUntil(timeoutMillis = E2ETimeouts.SHORT_MS) {
				compose.onAllNodes(hasTestTag(E2ETestTags.ONBOARDING_NEXT))
					.fetchSemanticsNodes().isNotEmpty()
			}
			compose.onNodeWithTag(E2ETestTags.ONBOARDING_NEXT).performClick()
			compose.waitForIdle()
		}
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.WELCOME_SCREEN))
				.fetchSemanticsNodes().isEmpty()
		}
	}

	private companion object {
		const val WELCOME_PAGE_COUNT = 3
	}
}
