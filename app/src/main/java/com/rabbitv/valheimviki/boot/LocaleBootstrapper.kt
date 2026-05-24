package com.rabbitv.valheimviki.boot

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.rabbitv.valheimviki.domain.model.language.AppLanguage
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Auto-pick the device locale only on a genuinely fresh install:
 *   1. AndroidX has never persisted a per-app locale (user never changed it), AND
 *   2. Onboarding has never been completed (so this is not an update from a prior version).
 *
 * Pre-existing users keep whatever they had (English by default) and will see the
 * language popup + settings tutorial overlay flows that ship with this update,
 * letting them opt in manually.
 *
 * Hilt-injectable so instrumented tests can drive it after seeding DataStore + a
 * [FakeLocaleProvider], rather than relying on [android.app.Application.onCreate]
 * which is bypassed by `HiltTestApplication`.
 */
@Singleton
class LocaleBootstrapper @Inject constructor(
	private val dataStoreUseCases: DataStoreUseCases,
	private val localeProvider: LocaleProvider,
) {
	fun run(force: Boolean = false) {
		if (!force && !AppCompatDelegate.getApplicationLocales().isEmpty) return

		runBlocking {
			val onboardingDone = dataStoreUseCases.readOnBoardingUseCase().first()
			if (onboardingDone) return@runBlocking

			val deviceLang = localeProvider.deviceLocale().language
			val picked = AppLanguage.entries.firstOrNull { it.code == deviceLang } ?: AppLanguage.ENGLISH

			AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(picked.code))
			dataStoreUseCases.saveLanguageState(picked.code)
		}
	}
}
