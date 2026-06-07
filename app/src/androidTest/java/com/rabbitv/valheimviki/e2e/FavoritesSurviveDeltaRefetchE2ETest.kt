package com.rabbitv.valheimviki.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.e2e.pages.BiomeDetailPage
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.FavoritesPage
import com.rabbitv.valheimviki.e2e.pages.LanguageNotificationPage
import com.rabbitv.valheimviki.e2e.pages.OnboardingPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Real-user E2E for the favorites-survive-delta-refetch invariant.
 *
 * Phase 1 (v1 backend, fresh install):
 *   onboarding (3 pages) → biomes grid → favorite biome-1..biome-5 via UI →
 *   favorites screen shows 5.
 *
 * Phase 2 (v2 backend, same session, biome-1 description changed server-side):
 *   trigger delta refetch (stands in for FetchWorker) → favorites screen STILL shows 5
 *   (REPLACE-on-conflict never deletes favorites) → biome-1 detail reflects v2 text.
 */
@HiltAndroidTest
class FavoritesSurviveDeltaRefetchE2ETest : BaseMockedE2ETest() {

	@Inject lateinit var dataRefetchUseCase: DataRefetchUseCase

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	override fun seedBeforeLaunch() {
		MockServerFixtures(mockServer).biomes(E2EFixtures.BIOMES_V1).install()
		runBlocking { dataRefetchUseCase.refetchAllData(forceRefresh = true) }
	}

	@Test
	fun favorites_persist_through_delta_refetch() {
		// --- Phase 1: v1 backend, user adds favorites ---
		OnboardingPage(compose).assertVisible().completeAll()
		LanguageNotificationPage(compose).completeFirstRunTutorial()
		val biomes = BiomesPage(compose).assertVisible()
		val detail = BiomeDetailPage(compose)

		E2EBiomes.FIVE_FAVORITE_IDS.forEach { id ->
			biomes.openBiome(id)
			detail.assertVisible().tapFavorite(expectedChecked = true).goBackToBiomes()
			biomes.assertVisible()
		}

		val favs = FavoritesPage(compose).open().assertVisible()
		favs.assertCount(
			expected = E2EBiomes.FIVE_FAVORITE_IDS.size,
			message = "Phase 1 favorites count"
		)
		E2EBiomes.FIVE_FAVORITE_IDS.forEach { id ->
			favs.assertContainsFavorite(id, "Phase 1: favorite $id missing")
		}

		// --- Phase 2: v2 backend delta-refetch (same activity) ---
		MockServerFixtures(mockServer).biomes(E2EFixtures.BIOMES_V2).install()
		runBlocking { dataRefetchUseCase.refetchAllData(forceRefresh = true) }
		compose.waitForIdle()

		favs.assertCount(
			expected = E2EBiomes.FIVE_FAVORITE_IDS.size,
			message = "Phase 2 favorites count after delta"
		)
		E2EBiomes.FIVE_FAVORITE_IDS.forEach { id ->
			favs.assertContainsFavorite(id, "Phase 2: favorite $id missing")
		}

		// Verify the modified biome's detail reflects the v2 marker.
		favs.openFavorite(E2EBiomes.MODIFIED_BIOME_ID)
		detail.assertVisible()
		compose.waitUntil(timeoutMillis = E2ETimeouts.SHORT_MS) {
			compose.onAllNodes(hasText(E2EFixtures.V2_DELTA_MARKER, substring = true))
				.fetchSemanticsNodes().isNotEmpty()
		}
		compose.onAllNodes(hasText(E2EFixtures.V2_DELTA_MARKER, substring = true))[0]
			.assertIsDisplayed()
	}
}
