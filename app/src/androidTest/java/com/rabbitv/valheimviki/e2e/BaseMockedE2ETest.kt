package com.rabbitv.valheimviki.e2e

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.WorkManagerTestInitHelper
import com.rabbitv.valheimviki.FetchWorkerFactory
import com.rabbitv.valheimviki.di.TestModuleConfig
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import javax.inject.Inject

/**
 * Base for deterministic E2E-style tests that use real Room/DataStore but mocked network.
 *
 * Keep these tests for contract edge cases that need exact server payload control.
 * Use [BaseRealE2ETest] for local Docker / production API scenarios.
 */
abstract class BaseMockedE2ETest {

	@get:Rule(order = 0)
	val hiltRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val prepRule: TestRule = object : TestWatcher() {
		override fun starting(description: Description?) {
			TestModuleConfig.useRealDatabase = true
			TestModuleConfig.useRealDataStore = true
			TestModuleConfig.useRealLocaleProvider = true
			TestModuleConfig.useRealNetwork = false

			wipeAppState()
			val instr = InstrumentationRegistry.getInstrumentation()
			instr.runOnMainSync {
				AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
			}
			hiltRule.inject()
			WorkManagerTestInitHelper.initializeTestWorkManager(
				targetContext,
				Configuration.Builder()
					.setMinimumLoggingLevel(Log.DEBUG)
					.setWorkerFactory(workerFactory)
					.build()
			)
			seedBeforeLaunch()
		}

		override fun finished(description: Description?) {
			runCatching { mockServer.shutdown() }
			wipeAppState()
			TestModuleConfig.reset()
		}
	}

	@Inject lateinit var mockServer: MockWebServer
	@Inject lateinit var workerFactory: FetchWorkerFactory

	protected val targetContext: Context
		get() = InstrumentationRegistry.getInstrumentation().targetContext

	protected open fun seedBeforeLaunch() {}

	private fun wipeAppState() {
		targetContext.deleteDatabase(DB_NAME)
		val dsFile = File(targetContext.filesDir, "datastore/$DS_NAME.preferences_pb")
		if (dsFile.exists()) dsFile.delete()
	}

	private companion object {
		const val DB_NAME = "valheimViki_database"
		const val DS_NAME = "valheimViki_preferences"
	}
}
