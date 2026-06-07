package com.rabbitv.valheimviki.testing

import com.rabbitv.valheimviki.data.repository.FakeDataStoreOperations
import kotlinx.coroutines.runBlocking

fun FakeDataStoreOperations.seedOnboardingTrue() = runBlocking { saveOnBoardingState(true) }
fun FakeDataStoreOperations.seedOnboardingFalse() = runBlocking { saveOnBoardingState(false) }
fun FakeDataStoreOperations.seedTooltipStep(step: Int) = runBlocking { saveSettingsTooltipStep(step) }
fun FakeDataStoreOperations.seedPopupShown(shown: Boolean) = runBlocking { saveLanguagePopupState(shown) }
fun FakeDataStoreOperations.seedLanguage(code: String) = runBlocking { saveLanguage(code) }
fun FakeDataStoreOperations.seedLastSuccessfulDataRefreshAt(timestampMillis: Long) =
	runBlocking { saveLastSuccessfulDataRefreshAt(timestampMillis) }
