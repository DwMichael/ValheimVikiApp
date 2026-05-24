package com.rabbitv.valheimviki.e2e

import androidx.test.core.app.ActivityScenario
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.FavoritesPage
import com.rabbitv.valheimviki.e2e.pages.OnboardingPage
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Ignore
import org.junit.Test

/**
 * Real user flow — favorites survive a delta refetch + Room migration.
 *
 * Scenario:
 *  1. Fresh install → splash → onboarding 3 pages → land on Biomes grid.
 *  2. User opens biome-1, taps Favorite. Repeat for biome-2..biome-5 (5 favorites).
 *  3. Kill process via `am force-stop` (uiautomator) + reopen with MockWebServer now serving v2.
 *  4. Assert: Favorites screen shows all 5 ids AND biome-1 detail reflects v2 description.
 *
 * Status: SKELETON. Tagged @Ignore until the following are in place:
 *  - testTag("BiomeItem_<id>") on each grid cell (DefaultGrid)
 *  - testTag("FavoriteToggle") on biome detail favorite button
 *  - testTag("FavoriteItem_<id>") on favorite grid items
 *  - testTag("nav_favorites") in MainAppBar (already have "nav_settings")
 *  - DataRefetchUseCase must run on second launch (currently shouldNotRefreshData guard may skip
 *    it if DB rows >= expected sizes). Either: trigger force refresh from UI on relaunch, OR
 *    let WorkManager run FetchWorker via TestDriver.setAllConstraintsMet.
 */
@HiltAndroidTest
@Ignore("Skeleton — see KDoc for outstanding prerequisites (refetch trigger + back-nav helper).")
class FavoritesSurviveDeltaRefetchE2ETest : BaseRealE2ETest() {

	@Test
	fun favorites_survive_delta_refetch_and_restart() {
		// --- Phase 1: fresh install, v1 backend ---
		MockServerFixtures(mockServer)
			.biomes("biomes_v1_en.json")
			.install()

		ActivityScenario.launch(MainActivity::class.java).use { scenario ->
			val onboarding = OnboardingPage(compose).assertVisible().completeAll()
			val biomes = BiomesPage(compose).assertVisible()

			// Favorite biome-1..biome-5. Requires testTag("BiomeItem_<id>") + favorite toggle on detail.
			listOf("biome-1", "biome-2", "biome-3", "biome-4", "biome-5").forEach { id ->
				biomes.openBiome(id)
				// TODO compose.onNodeWithTag("FavoriteToggle").performClick()
				// TODO compose.activity.onBackPressed() // or use back navigation
			}
		}

		// --- Phase 2: kill + relaunch w/ v2 backend (modified biome-1 description) ---
		// TODO: uiautomator `am force-stop com.rabbitv.valheimviki`
		MockServerFixtures(mockServer)
			.biomes("biomes_v2_en.json")
			.install()

		ActivityScenario.launch(MainActivity::class.java).use {
			val favs = FavoritesPage(compose).open().assertVisible()
			// TODO: assertEquals(5, favs.count())
			// TODO: assertTrue(favs.containsFavorite("biome-1"))
			// TODO: open biome-1 detail → assert description contains "[v2_updated]"
		}
	}
}
