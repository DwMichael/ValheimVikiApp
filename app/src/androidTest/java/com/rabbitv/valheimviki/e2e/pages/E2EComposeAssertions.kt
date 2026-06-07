package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.rabbitv.valheimviki.e2e.E2ETimeouts
import org.junit.Assert.assertTrue

fun ComposeTestRule.waitForNodeWithTag(tag: String, timeoutMillis: Long = E2ETimeouts.MEDIUM_MS) {
	waitUntil(timeoutMillis = timeoutMillis) {
		onAllNodes(hasTestTag(tag)).fetchSemanticsNodes().isNotEmpty()
	}
	onNodeWithTag(tag).assertIsDisplayed()
}

fun ComposeTestRule.waitForText(
	text: String,
	substring: Boolean = true,
	timeoutMillis: Long = E2ETimeouts.MEDIUM_MS
) {
	waitUntil(timeoutMillis = timeoutMillis) {
		onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty()
	}
	onAllNodes(hasText(text, substring = substring))[0].assertIsDisplayed()
}

fun ComposeTestRule.waitForImageLoaded(tag: String, timeoutMillis: Long = E2ETimeouts.VERY_LONG_MS) {
	waitUntil(timeoutMillis = timeoutMillis) {
		onAllNodes(imageState(tag, "loaded")).fetchSemanticsNodes().isNotEmpty()
	}
	assertTrue(
		"Image '$tag' should be loaded",
		onAllNodes(imageState(tag, "loaded")).fetchSemanticsNodes().isNotEmpty()
	)
}

private fun imageState(tag: String, expectedState: String): SemanticsMatcher =
	SemanticsMatcher("Image $tag has stateDescription=$expectedState") { node ->
		node.config.getOrNull(SemanticsProperties.TestTag) == tag &&
				node.config.getOrNull(SemanticsProperties.StateDescription) == expectedState
	}
