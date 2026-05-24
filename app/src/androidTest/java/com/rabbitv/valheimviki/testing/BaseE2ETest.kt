package com.rabbitv.valheimviki.testing

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.WorkManagerTestInitHelper
import com.rabbitv.valheimviki.boot.FakeLocaleProvider
import com.rabbitv.valheimviki.boot.LocaleBootstrapper
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import com.rabbitv.valheimviki.data.repository.FakeDataStoreOperations
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
abstract class BaseE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createEmptyComposeRule()

    @Inject lateinit var db: ValheimVikiDatabase
    @Inject lateinit var fakeDataStore: FakeDataStoreOperations
    @Inject lateinit var fakeLocale: FakeLocaleProvider
    @Inject lateinit var mockServer: MockWebServer
    @Inject lateinit var localeBootstrapper: LocaleBootstrapper

    @Before
    open fun setUp() {
        hiltRule.inject()
        val instr = InstrumentationRegistry.getInstrumentation()
        instr.runOnMainSync {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        }
        WorkManagerTestInitHelper.initializeTestWorkManager(
            instr.targetContext,
            Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .build()
        )
        fakeDataStore.reset()
        fakeLocale.setLocale(java.util.Locale.ENGLISH)
    }

    @After
    open fun tearDown() {
        db.close()
        mockServer.shutdown()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        }
    }
}
