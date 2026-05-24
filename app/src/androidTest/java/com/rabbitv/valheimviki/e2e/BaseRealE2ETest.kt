package com.rabbitv.valheimviki.e2e

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.WorkManagerTestInitHelper
import com.rabbitv.valheimviki.di.TestModuleConfig
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.File
import javax.inject.Inject

/**
 * Base class for **real** end-to-end tests.
 *
 * Differs from [com.rabbitv.valheimviki.testing.BaseE2ETest] (integration-test base):
 *  - On-disk Room database (survives Activity recreate / process kill)
 *  - On-disk DataStore (onboarding/popup state survives kill)
 *  - Real DefaultLocaleProvider
 *  - Only network is faked (MockWebServer)
 *
 * Test selection is driven by [TestModuleConfig] flags set in [setUp] BEFORE
 * `hiltRule.inject()` so the @Provides functions pick the real impls.
 *
 * Subclasses MUST be annotated with `@HiltAndroidTest`.
 *
 * Tests must drive UI like a real user. Do NOT poke DAOs / DataStore directly — use page objects.
 */
abstract class BaseRealE2ETest {

	@get:Rule(order = 0)
	val hiltRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val compose = createEmptyComposeRule()

	@Inject lateinit var mockServer: MockWebServer

	protected val targetContext: Context
		get() = InstrumentationRegistry.getInstrumentation().targetContext

	@Before
	open fun setUp() {
		TestModuleConfig.useRealDatabase = true
		TestModuleConfig.useRealDataStore = true
		TestModuleConfig.useRealLocaleProvider = true

		wipeAppState()
		val instr = InstrumentationRegistry.getInstrumentation()
		instr.runOnMainSync {
			AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
		}
		WorkManagerTestInitHelper.initializeTestWorkManager(
			targetContext,
			Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG).build()
		)
		hiltRule.inject()
	}

	@After
	open fun tearDown() {
		runCatching { mockServer.shutdown() }
		wipeAppState()
		TestModuleConfig.reset()
	}

	/** Removes the on-disk Room DB + DataStore preferences file. */
	protected fun wipeAppState() {
		targetContext.deleteDatabase(DB_NAME)
		val dsFile = File(targetContext.filesDir, "datastore/$DS_NAME.preferences_pb")
		if (dsFile.exists()) dsFile.delete()
	}

	companion object {
		private const val DB_NAME = "valheimViki_database"
		private const val DS_NAME = "valheimViki_preferences"
	}
}
