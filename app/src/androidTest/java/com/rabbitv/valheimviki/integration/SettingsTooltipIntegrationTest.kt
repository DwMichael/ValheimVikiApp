package com.rabbitv.valheimviki.integration

import androidx.compose.ui.test.*
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.testing.BaseE2ETest
import com.rabbitv.valheimviki.testing.seedOnboardingTrue
import com.rabbitv.valheimviki.testing.seedTooltipStep
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
class SettingsTooltipIntegrationTest : BaseE2ETest() {

    private val uiDevice: UiDevice
        get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val packageName = "com.rabbitv.valheimviki"

    /**
     * User who completed onboarding but never saw the tooltip.
     * Navigating to Settings should show step 1 overlay.
     * After clicking Next → step 2. After clicking Finish → overlay gone. Never shown again.
     */
    @Test
    fun freshUser_completesAllTooltipSteps_overlayDisappears() {
        fakeDataStore.seedOnboardingTrue()
        // tooltipStep=0 (default) → SettingsViewModel maps to step 1 (fresh start)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            // Navigate to Settings (assumes Settings tab/icon is accessible)
            // This tag must exist on the Settings navigation element in MainNavigation
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()

            // Step 1 overlay is visible
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()

            // Advance to step 2
            compose.onNodeWithTag("TutorialNextButton").performClick()
            compose.waitForIdle()

            // Step 2 overlay still visible
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()

            // Finish tutorial
            compose.onNodeWithTag("TutorialNextButton").performClick()
            compose.waitForIdle()

            // Overlay gone
            compose.onNodeWithTag("SettingsTutorialOverlay").assertDoesNotExist()

            // DataStore persisted TOOLTIP_COMPLETED = -1
            val step = runBlocking { fakeDataStore.readSettingsTooltipStep().first() }
            assertEquals(-1, step)
        }
    }

    /**
     * Kill app mid step 1 (simulate via force-stop). On re-launch, step 1 resumes.
     * Step is 0 (default) → ViewModel maps to 1. After step 1 started but before Next clicked,
     * the DataStore still holds 0 (step not yet saved). On relaunch, 0 → maps to 1 again.
     */
    @Test
    fun killMidStep1_resumesStep1OnReopen() {
        fakeDataStore.seedOnboardingTrue()
        // DataStore step = 0 → ViewModel shows step 1

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()
            // Do NOT click Next — simulate kill by closing scenario
        }

        // Re-launch — DataStore still has 0 → shows step 1 again
        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()
        }
    }

    /**
     * Kill app mid step 2. DataStore has 2 persisted. On re-launch step 2 is shown.
     */
    @Test
    fun killMidStep2_resumesStep2OnReopen() {
        fakeDataStore.seedOnboardingTrue()
        fakeDataStore.seedTooltipStep(2) // Persisted after step 2 started

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            // ViewModel reads step=2, shows step 2 overlay directly
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()
        }
    }

    /**
     * User who already completed the tutorial (step=-1). Overlay must never appear.
     */
    @Test
    fun completedTooltip_neverShowsAgain() {
        fakeDataStore.seedOnboardingTrue()
        fakeDataStore.seedTooltipStep(-1) // TOOLTIP_COMPLETED

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            compose.onNodeWithTag("SettingsTutorialOverlay").assertDoesNotExist()
        }
    }

    /**
     * Force-stop via UiAutomator — true process-death simulation.
     * Seed step=2 in DataStore before launch. Kill. Reopen. Overlay still at step 2.
     */
    @org.junit.Ignore("am force-stop kills the instrumentation process (shares PID with target). Move to a true E2E test with separate process / uiautomator-only flow.")
    @Test
    fun uiAutomatorKill_resumesCorrectStep() {
        fakeDataStore.seedOnboardingTrue()

        // First launch: advance to step 2 and let it persist
        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            // Step 1 shown → click Next → step 2 saved
            compose.onNodeWithTag("TutorialNextButton").performClick()
            compose.waitForIdle()
        }

        // Force stop simulating real process death
        uiDevice.pressHome()
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("am force-stop $packageName")
            .close()

        // Re-launch — should resume step 2
        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitForIdle()
            compose.onNodeWithTag("nav_settings").performClick()
            compose.waitForIdle()
            compose.onNodeWithTag("SettingsTutorialOverlay").assertIsDisplayed()
            // DataStore step=2 → ViewModel shows step 2
            val step = runBlocking { fakeDataStore.readSettingsTooltipStep().first() }
            assertEquals(2, step)
        }
    }
}
