package com.rabbitv.valheimviki.integration

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.core.os.LocaleListCompat
import androidx.test.core.app.ActivityScenario
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.testing.BaseE2ETest
import com.rabbitv.valheimviki.testing.seedLanguage
import com.rabbitv.valheimviki.testing.seedOnboardingTrue
import com.rabbitv.valheimviki.testing.seedPopupShown
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

@HiltAndroidTest
class UpdatePathLocaleIntegrationTest : BaseE2ETest() {

    /**
     * Existing user who never changed language. Device locale is Polish.
     * App must NOT auto-switch to Polish (onboarding=true guards this).
     * Language popup MUST appear (popup_shown=false by default for this user).
     */
    @Test
    fun oldUser_neverChangedLanguage_keepsSystemDefault_andSeesPopup() {
        fakeDataStore.seedOnboardingTrue()
        // popup_shown remains false (default from reset)
        fakeLocale.setLocale(Locale.forLanguageTag("pl"))
        // Per-app locale is empty (no user-set locale via AndroidX)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())

        localeBootstrapper.run()

        // run() must NOT set locale (guarded by onboarding=true)
        assertTrue(AppCompatDelegate.getApplicationLocales().isEmpty)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            // Language popup shows for old users when popup_shown=false
            compose.onNodeWithTag("LanguageNotificationDialog").assertIsDisplayed()
        }
    }

    /**
     * Old user who previously picked German via Settings.
     * Per-app locale is already DE. Popup shows (new key, first-time for this user).
     */
    @Test
    fun oldUser_pickedGermanBefore_staysGerman_popupStillFires() {
        fakeDataStore.seedOnboardingTrue()
        fakeDataStore.seedLanguage("de")
        // popup_shown=false (default)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("de"))
        fakeLocale.setLocale(Locale.forLanguageTag("pl"))

        localeBootstrapper.run()

        // Saved language must remain "de" — bootstrapper must not overwrite a pre-seeded value.
        // Asserting on FakeDataStore instead of AppCompatDelegate because API 33+ LocaleManager
        // roundtrips through binder and is not reliably visible in the instrumentation process.
        val savedLang = runBlocking { fakeDataStore.languageProvider().first() }
        assertEquals("de", savedLang)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("LanguageNotificationDialog").assertIsDisplayed()
        }
    }

    /**
     * Old user who already dismissed the popup.
     * Re-launch must NOT show popup again.
     */
    @Test
    fun oldUser_popupAlreadyDismissed_doesNotShowOnRelaunch() {
        fakeDataStore.seedOnboardingTrue()
        fakeDataStore.seedPopupShown(true) // already dismissed

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("LanguageNotificationDialog").assertDoesNotExist()
        }
    }
}
