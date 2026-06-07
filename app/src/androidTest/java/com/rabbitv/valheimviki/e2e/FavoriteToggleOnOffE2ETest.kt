package com.rabbitv.valheimviki.e2e

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
 * Edge case: user favorites then un-favorites an item. The favorites screen must
 * reflect the removal.
 *
 * Production risk: stale UI state after toggle-off means users cannot remove favorites.
 */
@HiltAndroidTest
class FavoriteToggleOnOffE2ETest : BaseMockedE2ETest() {

	@Inject lateinit var dataRefetchUseCase: DataRefetchUseCase

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	override fun seedBeforeLaunch() {
		MockServerFixtures(mockServer).biomes(E2EFixtures.BIOMES_V1).install()
		runBlocking { dataRefetchUseCase.refetchAllData(forceRefresh = true) }
	}

	@Test
	fun toggling_favorite_off_removes_item_from_favorites() {
		OnboardingPage(compose).assertVisible().completeAll()
		LanguageNotificationPage(compose).completeFirstRunTutorial()
		val biomes = BiomesPage(compose).assertVisible()
		val detail = BiomeDetailPage(compose)
		val favs = FavoritesPage(compose)

		val targetId = E2EBiomes.MODIFIED_BIOME_ID

		// Add favorite via biome detail
		biomes.openBiome(targetId)
		detail.assertVisible().tapFavorite(expectedChecked = true).goBackToBiomes()
		biomes.assertVisible()

		favs.open().assertVisible()
		favs.assertCount(expected = 1, message = "after favorite tap: 1 item in favorites")

		// Toggle off via same detail screen (entered from favorites)
		favs.openFavorite(targetId)
		detail.assertVisible().tapFavorite(expectedChecked = false).goBackToFavorites()

		favs.assertVisible()
		favs.assertCount(expected = 0, message = "after toggle off: favorites empty")
	}
}
