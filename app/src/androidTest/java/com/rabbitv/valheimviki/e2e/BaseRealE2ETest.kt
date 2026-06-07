package com.rabbitv.valheimviki.e2e

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.concurrent.futures.await
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.TestDriver
import androidx.work.WorkInfo
import androidx.work.testing.WorkManagerTestInitHelper
import com.rabbitv.valheimviki.FetchWorker
import com.rabbitv.valheimviki.FetchWorkerFactory
import com.rabbitv.valheimviki.di.TestModuleConfig
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import javax.inject.Inject

/**
 * Base for real-network end-to-end tests.
 *
 *  - On-disk Room database (survives Activity recreate / process kill)
 *  - On-disk DataStore
 *  - Real DefaultLocaleProvider
 *  - Retrofit uses BuildConfig.baseUrlSafe or instrumentation arg `e2eBaseUrl`
 *
 * Rule ordering (lower order = outer rule, runs first):
 *  - 0: HiltAndroidRule — wires the test graph.
 *  - 1: prepRule — wipes app state, sets [TestModuleConfig] flags so the
 *       runtime-toggleable @Provides functions pick real impls, initializes
 *       WorkManager (MainActivity.onCreate needs it), calls
 *       `hiltRule.inject()` and then [seedBeforeLaunch] so the subclass can
 *       pre-populate Room / DataStore BEFORE the activity is launched.
 *  - 2 (subclass): `createAndroidComposeRule<MainActivity>()` — launches
 *       MainActivity and binds the Compose test infrastructure to its main
 *       thread (avoids `setCurrentState must be called on the main thread`).
 *
 * Subclass MUST:
 *  - be annotated `@HiltAndroidTest`
 *  - declare `@get:Rule(order = 2) val compose = createAndroidComposeRule<MainActivity>()`
 *  - override [seedBeforeLaunch] if it needs Room/DataStore pre-populated.
 *
 * Drive UI like a real user. Page objects in `e2e/pages/` are preferred over raw selectors.
 */
abstract class BaseRealE2ETest {

	@get:Rule(order = 0)
	val hiltRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val prepRule: TestRule = object : TestWatcher() {
		override fun starting(description: Description?) {
			TestModuleConfig.useRealDatabase = true
			TestModuleConfig.useRealDataStore = true
			TestModuleConfig.useRealLocaleProvider = true
			TestModuleConfig.useRealNetwork = true

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
			wipeAppState()
			TestModuleConfig.reset()
		}
	}

	protected val targetContext: Context
		get() = InstrumentationRegistry.getInstrumentation().targetContext

	protected val workManagerTestDriver: TestDriver?
		get() = WorkManagerTestInitHelper.getTestDriver(targetContext)

	@Inject
	lateinit var workerFactory: FetchWorkerFactory

	/** Override to pre-populate Room / DataStore BEFORE MainActivity launches. */
	protected open fun seedBeforeLaunch() {}
	protected suspend fun runInitialFetchWorker() {
		val workManager = WorkManager.getInstance(targetContext)
		// .await() elegantly unwrap the ListenableFuture into a standard Kotlin List
		val workInfos = workManager
			.getWorkInfosByTag(FetchWorker.INITIAL_FETCH_WORK_TAG)
			.await()

		val workInfo = workInfos.firstOrNull()
			?: error("No work found with tag ${FetchWorker.INITIAL_FETCH_WORK_TAG}")

		val testDriver = workManagerTestDriver
			?: error("WorkManager TestDriver is not available")

		testDriver.setInitialDelayMet(workInfo.id)

		withTimeout(WORK_COMPLETION_TIMEOUT_MS) {
			while (true) {
				val state = workManager.getWorkInfoById(workInfo.id).await()?.state
				if (state == WorkInfo.State.SUCCEEDED) return@withTimeout
				check(state != WorkInfo.State.FAILED && state != WorkInfo.State.CANCELLED) {
					"FetchWorker terminated with state=$state"
				}
				delay(WORK_POLL_INTERVAL_MS)
			}
		}
	}

	private fun wipeAppState() {
		targetContext.deleteDatabase(DB_NAME)
		val dsFile = File(targetContext.filesDir, "datastore/$DS_NAME.preferences_pb")
		if (dsFile.exists()) dsFile.delete()
	}

	companion object {
		private const val DB_NAME = "valheimViki_database"
		private const val DS_NAME = "valheimViki_preferences"
		private const val WORK_COMPLETION_TIMEOUT_MS = 60_000L
		private const val WORK_POLL_INTERVAL_MS = 100L
	}
}
