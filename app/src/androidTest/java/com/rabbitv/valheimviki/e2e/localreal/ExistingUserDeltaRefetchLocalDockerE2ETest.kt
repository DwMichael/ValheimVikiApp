package com.rabbitv.valheimviki.e2e.localreal

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.e2e.BaseRealE2ETest
import com.rabbitv.valheimviki.e2e.E2ERealData
import com.rabbitv.valheimviki.e2e.pages.BiomeDetailPage
import com.rabbitv.valheimviki.e2e.pages.BiomesPage
import com.rabbitv.valheimviki.e2e.pages.FavoritesPage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ExistingUserDeltaRefetchLocalDockerE2ETest : BaseRealE2ETest() {

	@Inject lateinit var dataRefetchUseCase: DataRefetchUseCase
	@Inject lateinit var dataStoreUseCases: DataStoreUseCases
	@Inject lateinit var db: ValheimVikiDatabase

	@get:Rule(order = 2)
	val compose = createAndroidComposeRule<MainActivity>()

	override fun seedBeforeLaunch() {
		runBlocking {
			dataStoreUseCases.saveOnBoardingState(true)
			dataStoreUseCases.saveLanguagePopupState(true)
			dataStoreUseCases.saveSettingsTooltipState(TOOLTIP_COMPLETED)

			val result = dataRefetchUseCase.refetchAllData(forceRefresh = true)
			check(result is DataRefetchResult.Success) {
				"Initial real Docker refetch failed: $result"
			}

			val biome = db.biomeDao().getBiomeById(E2ERealData.MEADOWS_ID).first()
				?: error("Meadows was not stored in Room")
			db.favoriteDao().addFavorite(biome.toFavorite())
			dataStoreUseCases.saveLastSuccessfulDataRefreshAt(
				System.currentTimeMillis() - STALE_REFRESH_AGE_MS
			)
		}
	}

	@Test
	fun existing_user_receives_server_delta_and_keeps_favorite_navigation() {
		val postgres = LocalDockerPostgres()
		val originalDescription = postgres.readText(E2ERealData.MEADOWS_DESCRIPTION_TEXT_ID)
		val mutatedDescription = "$originalDescription\n\n${E2ERealData.DELTA_MARKER}"

		try {
			postgres.updateText(E2ERealData.MEADOWS_DESCRIPTION_TEXT_ID, mutatedDescription)
			runBlocking { runInitialFetchWorker() }

			BiomesPage(compose).assertVisible()
			val favorites = FavoritesPage(compose).open().assertVisible()
			favorites
				.assertCount(expected = 1, message = "existing user favorite should survive refetch")
				.assertFavoriteLoaded(E2ERealData.MEADOWS_ID, E2ERealData.MEADOWS_NAME)
				.openFavorite(E2ERealData.MEADOWS_ID)

			BiomeDetailPage(compose)
				.assertContent(
					id = E2ERealData.MEADOWS_ID,
					title = E2ERealData.MEADOWS_NAME,
					descriptionFragment = E2ERealData.DELTA_MARKER
				)
		} finally {
			postgres.updateText(E2ERealData.MEADOWS_DESCRIPTION_TEXT_ID, originalDescription)
		}
	}

	private companion object {
		const val TOOLTIP_COMPLETED = -1
		const val STALE_REFRESH_AGE_MS = 48L * 60L * 60L * 1000L
	}
}
