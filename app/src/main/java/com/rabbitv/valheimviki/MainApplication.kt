package com.rabbitv.valheimviki

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.android.gms.ads.MobileAds
import com.rabbitv.valheimviki.domain.model.language.AppLanguage
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

	@Inject
	lateinit var workerFactory: FetchWorkerFactory

	@Inject
	lateinit var dataStoreUseCases: DataStoreUseCases

	private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

	override fun onCreate() {
		super.onCreate()
		applyDeviceLocaleOnFirstLaunch()
		MobileAds.initialize(this) {}
	}

	/**
	 * Auto-pick the device locale only on a genuinely fresh install:
	 *   1. AndroidX has never persisted a per-app locale (user never changed it), AND
	 *   2. Onboarding has never been completed (so this is not an update from a prior version).
	 * Pre-existing users keep whatever they had (English by default) and will see the
	 * language popup + settings tutorial overlay flows that ship with this update,
	 * letting them opt in manually.
	 */
	private fun applyDeviceLocaleOnFirstLaunch() {
		if (!AppCompatDelegate.getApplicationLocales().isEmpty) return

		val onboardingDone = runBlocking {
			dataStoreUseCases.readOnBoardingUseCase().first()
		}
		if (onboardingDone) return

		val deviceLang = Resources.getSystem().configuration.locales[0]?.language ?: "en"
		val picked = AppLanguage.entries.firstOrNull { it.code == deviceLang } ?: AppLanguage.ENGLISH

		AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(picked.code))
		appScope.launch {
			dataStoreUseCases.saveLanguageState(picked.code)
		}
	}

	override val workManagerConfiguration: Configuration
		get() = Configuration.Builder()
			.setMinimumLoggingLevel(Log.DEBUG)
			.setWorkerFactory(workerFactory)
			.build()
}


class FetchWorkerFactory @Inject constructor(
	private val refetchUseCase: DataRefetchUseCase,
) : WorkerFactory() {
	override fun createWorker(
		appContext: Context,
		workerClassName: String,
		workerParameters: WorkerParameters
	): ListenableWorker? = FetchWorker(refetchUseCase, appContext, workerParameters)
}
