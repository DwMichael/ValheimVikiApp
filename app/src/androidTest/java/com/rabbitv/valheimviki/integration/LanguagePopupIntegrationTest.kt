package com.rabbitv.valheimviki.integration

import androidx.compose.ui.test.*
import androidx.test.core.app.ActivityScenario
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.testing.BaseE2ETest
import com.rabbitv.valheimviki.testing.seedOnboardingTrue
import com.rabbitv.valheimviki.testing.seedPopupShown
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class LanguagePopupIntegrationTest : BaseE2ETest() {

    /**
     * Old user (onboarding=true, popup never shown).
     * Popup must appear on first launch after update.
     */
    @Test
    fun oldUser_firstLaunchAfterUpdate_popupShows() {
        fakeDataStore.seedOnboardingTrue()
        // popup_shown=false (reset default)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("LanguageNotificationDialog").assertIsDisplayed()
        }
    }

    /**
     * Old user who already dismissed popup. Must not see it again.
     */
    @Test
    fun oldUser_popupAlreadyDismissed_doesNotReturn() {
        fakeDataStore.seedOnboardingTrue()
        fakeDataStore.seedPopupShown(true)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("LanguageNotificationDialog").assertDoesNotExist()
        }
    }

    /**
     * Fresh install user (onboarding=false).
     * Popup must NOT show — it's gated on onboarding=true.
     */
    @Test
    fun freshInstall_onboardingNotComplete_popupDoesNotShow() {
        // onboarding=false (reset default)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("LanguageNotificationDialog").assertDoesNotExist()
        }
    }
}
