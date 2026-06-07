package com.rabbitv.valheimviki.e2e.localreal

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.e2e.BaseRealE2ETest
import com.rabbitv.valheimviki.e2e.E2ERealData
import com.rabbitv.valheimviki.e2e.pages.BiomeDetailPage
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.FavoritesPage
import com.rabbitv.valheimviki.e2e.pages.GenericDetailPage
import com.rabbitv.valheimviki.e2e.pages.LanguageNotificationPage
import com.rabbitv.valheimviki.e2e.pages.OnboardingPage
import com.rabbitv.valheimviki.e2e.pages.SearchPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LocalDockerFreshUserCatalogFavoritesE2ETest : BaseRealE2ETest() {

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	@Test
	fun fresh_user_loads_real_catalog_favorites_and_detail_navigation() {
		runBlocking { runInitialFetchWorker() }

		OnboardingPage(compose).assertVisible().completeAll()
		LanguageNotificationPage(compose).completeFirstRunTutorial()

		val biomes = BiomesPage(compose).assertVisible()
		biomes.assertBiomeLoaded(E2ERealData.MEADOWS_ID, E2ERealData.MEADOWS_NAME)
		biomes.assertBiomeLoaded(E2ERealData.BLACK_FOREST_ID, "Black Forest")

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
			.assertCount(expected = 1, message = "fresh user should have one favorite")
			.assertFavoriteLoaded(E2ERealData.MEADOWS_ID, E2ERealData.MEADOWS_NAME)
			.openFavorite(E2ERealData.MEADOWS_ID)

		detail.assertContent(
			id = E2ERealData.MEADOWS_ID,
			title = E2ERealData.MEADOWS_NAME,
			descriptionFragment = E2ERealData.MEADOWS_DESCRIPTION_FRAGMENT
		)
		detail.goBackToFavorites()
		GenericDetailPage(compose).goBack()

		val search = SearchPage(compose).open()
		search.search(E2ERealData.GREYDWARF_NAME).openResult(E2ERealData.GREYDWARF_ID)
		GenericDetailPage(compose)
			.assertContent(E2ERealData.GREYDWARF_NAME, E2ERealData.GREYDWARF_DESCRIPTION_FRAGMENT)
			.goBack()

		search.search(E2ERealData.EIKTHYR_ALTAR_NAME).openResult(E2ERealData.EIKTHYR_ALTAR_ID)
		GenericDetailPage(compose)
			.assertContent(
				E2ERealData.EIKTHYR_ALTAR_NAME,
				E2ERealData.EIKTHYR_ALTAR_DESCRIPTION_FRAGMENT
			)
	}
}
