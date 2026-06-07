package com.rabbitv.valheimviki.e2e

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.LanguageNotificationPage
import com.rabbitv.valheimviki.e2e.pages.OnboardingPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Edge case: user finishes onboarding then the process is restarted (simulated via
 * [androidx.test.core.app.ActivityScenario.recreate]). On reopen the welcome carousel
 * MUST NOT show again.
 *
 * Production risk: a regression here means every app launch forces returning users
 * back through the 3-page tutorial.
 */
@HiltAndroidTest
class OnboardingPersistsAcrossRecreateE2ETest : BaseMockedE2ETest() {

	@Inject lateinit var dataRefetchUseCase: DataRefetchUseCase

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	override fun seedBeforeLaunch() {
		MockServerFixtures(mockServer).biomes(E2EFixtures.BIOMES_V1).install()
		runBlocking { dataRefetchUseCase.refetchAllData(forceRefresh = true) }
	}

	@Test
	fun onboarding_completion_persists_across_activity_recreate() {
		OnboardingPage(compose).assertVisible().completeAll()
		LanguageNotificationPage(compose).completeFirstRunTutorial()
		BiomesPage(compose).assertVisible()

		compose.activityRule.scenario.recreate()
		compose.waitForIdle()

		BiomesPage(compose).assertVisible()
		assertTrue(
			"WelcomeScreen must not reappear after recreate (onboarding state persisted)",
			compose.onAllNodes(hasTestTag(E2ETestTags.WELCOME_SCREEN))
				.fetchSemanticsNodes().isEmpty()
		)
		LanguageNotificationPage(compose).assertNotVisible()
	}
}
