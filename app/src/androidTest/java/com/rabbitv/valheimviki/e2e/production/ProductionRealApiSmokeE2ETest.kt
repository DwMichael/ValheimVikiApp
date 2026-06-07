package com.rabbitv.valheimviki.e2e.production

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.rabbitv.valheimviki.BuildConfig
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.e2e.BaseRealE2ETest
import com.rabbitv.valheimviki.e2e.E2ERealData
import com.rabbitv.valheimviki.e2e.pages.BiomeDetailPage
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.FavoritesPage
import com.rabbitv.valheimviki.e2e.pages.LanguageNotificationPage
import com.rabbitv.valheimviki.e2e.pages.OnboardingPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProductionRealApiSmokeE2ETest : BaseRealE2ETest() {

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	@Test
	fun production_api_loads_real_data_and_favorite_round_trip_works() {
		assumeTrue("Production smoke only runs on real_app_varian", BuildConfig.FLAVOR == "real_app_varian")
		runBlocking { runInitialFetchWorker() }

		OnboardingPage(compose).assertVisible().completeAll()
		LanguageNotificationPage(compose).completeFirstRunTutorial()

		val biomes = BiomesPage(compose)
			.assertVisible()
			.assertBiomeLoaded(E2ERealData.MEADOWS_ID, E2ERealData.MEADOWS_NAME)

		val detail = BiomeDetailPage(compose)
		biomes.openBiome(E2ERealData.MEADOWS_ID)
		detail
			.assertContent(
				id = E2ERealData.MEADOWS_ID,
				title = E2ERealData.MEADOWS_NAME,
				descriptionFragment = E2ERealData.MEADOWS_DESCRIPTION_FRAGMENT
			)
			.tapFavorite(expectedChecked = true)
			.goBackToBiomes()

		val favorites = FavoritesPage(compose).open().assertVisible()
		favorites
			.assertCount(expected = 1, message = "production smoke favorite count")
			.assertFavoriteLoaded(E2ERealData.MEADOWS_ID, E2ERealData.MEADOWS_NAME)
			.openFavorite(E2ERealData.MEADOWS_ID)

		detail
			.assertContent(
				id = E2ERealData.MEADOWS_ID,
				title = E2ERealData.MEADOWS_NAME,
				descriptionFragment = E2ERealData.MEADOWS_DESCRIPTION_FRAGMENT
			)
			.tapFavorite(expectedChecked = false)
			.goBackToFavorites()

		favorites.assertVisible().assertCount(expected = 0, message = "favorite removed")
	}
}
