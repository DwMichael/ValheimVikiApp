package com.rabbitv.valheimviki.boot

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.rabbitv.valheimviki.domain.model.language.AppLanguage
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.utils.Constants.DEFAULT_LANG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Keeps Android resources and cached game data on the same language.
 *
 * On a genuinely fresh install, the app picks the device locale when it is supported.
 * On later launches, it reapplies the language saved in DataStore if AppCompat has no
 * active per-app locale yet.
 *
 * Hilt-injectable so instrumented tests can drive it after seeding DataStore + a
 * [FakeLocaleProvider], rather than relying on production startup wiring.
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
			val savedLanguage = dataStoreUseCases.languageProvider().first().ifBlank { DEFAULT_LANG }
			val picked = if (!force && onboardingDone) {
				AppLanguage.fromCode(savedLanguage)
			} else {
				pickDeviceLanguage()
			}

			AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(picked.code))
			if (force || !onboardingDone || savedLanguage != picked.code) {
				dataStoreUseCases.saveLanguageState(picked.code)
			}
		}
	}

	private fun pickDeviceLanguage(): AppLanguage {
		val deviceLang = localeProvider.deviceLocale().language
		return AppLanguage.entries.firstOrNull { it.code == deviceLang } ?: AppLanguage.ENGLISH
	}
}
