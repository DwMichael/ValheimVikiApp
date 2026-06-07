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

class LanguageNotificationPage(private val compose: ComposeTestRule) {

	fun assertVisible(): LanguageNotificationPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.LONG_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.LANGUAGE_NOTIFICATION_DIALOG))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.LANGUAGE_NOTIFICATION_DIALOG).assertIsDisplayed()
	}

	fun assertNotVisible(): LanguageNotificationPage = apply {
		compose.onNodeWithTag(E2ETestTags.LANGUAGE_NOTIFICATION_DIALOG).assertDoesNotExist()
	}

	fun dismissWhenReady(): LanguageNotificationPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.LONG_MS) {
			compose.onAllNodes(dismissButtonEnabled()).fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.LANGUAGE_NOTIFICATION_DISMISS).performClick()
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.LANGUAGE_NOTIFICATION_DIALOG))
				.fetchSemanticsNodes().isEmpty()
		}
	}

	fun assertSettingsHighlightBlockingContent(): LanguageNotificationPage = apply {
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.LANGUAGE_SETTINGS_HIGHLIGHT_BLOCKER))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(E2ETestTags.LANGUAGE_SETTINGS_HIGHLIGHT_BLOCKER)
			.assertIsDisplayed()
	}

	fun completeFirstRunTutorial(): LanguageNotificationPage = apply {
		assertVisible()
		dismissWhenReady()
		assertSettingsHighlightBlockingContent()
		SettingsPage(compose)
			.openFromHighlightedSettings()
			.completeTutorial()
			.goBackToBiomes()
	}

	private fun dismissButtonEnabled(): SemanticsMatcher =
		SemanticsMatcher("Language notice dismiss button is enabled") { node ->
			node.config.getOrNull(SemanticsProperties.TestTag) ==
					E2ETestTags.LANGUAGE_NOTIFICATION_DISMISS &&
					!node.config.contains(SemanticsProperties.Disabled)
		}
}
