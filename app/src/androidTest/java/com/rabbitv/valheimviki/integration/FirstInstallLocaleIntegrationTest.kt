package com.rabbitv.valheimviki.integration

import com.rabbitv.valheimviki.testing.BaseE2ETest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

/**
 * Locale bootstrap is asserted via FakeDataStore (deterministic). AppCompatDelegate
 * locale state is not asserted because on API 33+ it round-trips through the system
 * LocaleManager and is not reliably visible in the instrumentation process.
 */
@HiltAndroidTest
class FirstInstallLocaleIntegrationTest : BaseE2ETest() {

    @Test
    fun freshInstall_polishDevice_picksPolish() {
        fakeLocale.setLocale(Locale.forLanguageTag("pl"))
        localeBootstrapper.run(force = true)

        val savedLang = runBlocking { fakeDataStore.languageProvider().first() }
        assertEquals("pl", savedLang)
    }

    @Test
    fun freshInstall_unsupportedDevice_fallsBackToEnglish() {
        fakeLocale.setLocale(Locale.forLanguageTag("ja")) // Japanese — not in AppLanguage list
        localeBootstrapper.run(force = true)

        val savedLang = runBlocking { fakeDataStore.languageProvider().first() }
        assertEquals("en", savedLang)
    }

    @Test
    fun freshInstall_germanDevice_picksGerman() {
        fakeLocale.setLocale(Locale.forLanguageTag("de"))
        localeBootstrapper.run(force = true)

        val savedLang = runBlocking { fakeDataStore.languageProvider().first() }
        assertEquals("de", savedLang)
    }

    @Test
    fun freshInstall_lithuanianDevice_picksLithuanian() {
        fakeLocale.setLocale(Locale.forLanguageTag("lt"))
        localeBootstrapper.run(force = true)

        val savedLang = runBlocking { fakeDataStore.languageProvider().first() }
        assertEquals("lt", savedLang)
    }
}
