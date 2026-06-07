package com.rabbitv.valheimviki.e2e.pages

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.rabbitv.valheimviki.e2e.E2ETestTags
import com.rabbitv.valheimviki.e2e.E2ETimeouts

class GenericDetailPage(private val compose: ComposeTestRule) {

	fun assertContent(title: String, descriptionFragment: String): GenericDetailPage = apply {
		compose.waitForText(title, substring = false, timeoutMillis = E2ETimeouts.VERY_LONG_MS)
		compose.waitForText(descriptionFragment, substring = true, timeoutMillis = E2ETimeouts.VERY_LONG_MS)
	}

	fun goBack(): GenericDetailPage = apply {
		val hasAppBack = compose.onAllNodes(hasTestTag(E2ETestTags.DETAIL_BACK))
			.fetchSemanticsNodes().isNotEmpty()
		if (hasAppBack) {
			compose.onNodeWithTag(E2ETestTags.DETAIL_BACK).performClick()
		} else {
			Espresso.pressBack()
		}
		compose.waitForIdle()
	}
}
