package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

class SearchPage(private val compose: ComposeTestRule) {

	fun open(): SearchPage = apply {
		compose.waitForNodeWithTag(E2ETestTags.NAV_SEARCH)
		compose.onNodeWithTag(E2ETestTags.NAV_SEARCH).performClick()
		compose.waitForNodeWithTag(E2ETestTags.SEARCH_INPUT)
	}

	fun search(query: String): SearchPage = apply {
		compose.onNodeWithTag(E2ETestTags.SEARCH_INPUT).performTextClearance()
		compose.onNodeWithTag(E2ETestTags.SEARCH_INPUT).performTextInput(query)
		compose.waitForIdle()
	}

	fun openResult(id: String): SearchPage = apply {
		val tag = E2ETestTags.listItem(id)
		compose.waitUntil(timeoutMillis = E2ETimeouts.VERY_LONG_MS) {
			compose.onAllNodes(hasTestTag(tag)).fetchSemanticsNodes().isNotEmpty()
		}
		compose.onNodeWithTag(tag).performClick()
		compose.waitForIdle()
	}
}
