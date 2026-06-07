package com.rabbitv.valheimviki.e2e

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.LanguageNotificationPage
import com.rabbitv.valheimviki.e2e.pages.SettingsPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Real case: existing user updates to a version that introduces multi-language support.
 * On their first launch after update they must see the language-picker popup (gated on
 * `onboarding=true AND popup_shown=false`).
 *
 * Production risk: a regression here means existing users never discover the new
 * language feature after upgrade.
 */
@HiltAndroidTest
class LanguagePopupForUpgraderE2ETest : BaseMockedE2ETest() {

	@Inject lateinit var dataStore: DataStoreOperations
	@Inject lateinit var dataRefetchUseCase: DataRefetchUseCase

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	override fun seedBeforeLaunch() {
		MockServerFixtures(mockServer).biomes(E2EFixtures.BIOMES_V1).install()
		runBlocking {
			dataRefetchUseCase.refetchAllData(forceRefresh = true)
			// Returning user: onboarding completed before this update, popup never seen.
			dataStore.saveOnBoardingState(true)
		}
	}

	@Test
	fun language_popup_blocks_app_until_notice_and_settings_prompt_are_handled() {
		val biomes = BiomesPage(compose).assertVisible()
		val languageNotice = LanguageNotificationPage(compose).assertVisible()

		runCatching {
			compose.onNodeWithTag(E2ETestTags.NAV_FAVORITES).performClick()
		}
		compose.waitForIdle()

		languageNotice.assertVisible()
		compose.onNodeWithTag(E2ETestTags.FAVORITE_GRID_SCAFFOLD).assertDoesNotExist()

		languageNotice.dismissWhenReady()
			.assertSettingsHighlightBlockingContent()

		runCatching {
			biomes.openBiome(E2EBiomes.MODIFIED_BIOME_ID)
		}
		compose.waitForIdle()

		compose.onNodeWithTag(E2ETestTags.BIOME_DETAIL_SCREEN).assertDoesNotExist()

		SettingsPage(compose)
			.openFromHighlightedSettings()
			.completeTutorial()
			.goBackToBiomes()

		biomes.openBiome(E2EBiomes.MODIFIED_BIOME_ID)
		compose.waitUntil(timeoutMillis = E2ETimeouts.MEDIUM_MS) {
			compose.onAllNodes(hasTestTag(E2ETestTags.BIOME_DETAIL_SCREEN))
				.fetchSemanticsNodes().isNotEmpty()
		}
	}
}
