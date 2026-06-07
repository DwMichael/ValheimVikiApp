package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
	private val dataStore: DataStoreOperations,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
	suspend fun saveOnBoardingState(completed: Boolean) {
		withContext(ioDispatcher)
		{
			dataStore.saveOnBoardingState(completed = completed)
		}

	}

	fun readOnBoardingState(): Flow<Boolean> {
		return dataStore.readOnBoardingState()
	}

	suspend fun saveLanguage(language: String) {
		withContext(ioDispatcher)
		{
			dataStore.saveLanguage(language = language)
		}
	}

	fun languageProvider(): Flow<String> {
		return dataStore.languageProvider()
	}

	suspend fun saveDataLanguage(language: String) {
		withContext(ioDispatcher)
		{
			dataStore.saveDataLanguage(language = language)
		}
	}

	fun dataLanguageProvider(): Flow<String> {
		return dataStore.dataLanguageProvider()
	}

	suspend fun saveLanguagePopupState(shown: Boolean) {
		withContext(ioDispatcher) {
			dataStore.saveLanguagePopupState(shown)
		}
	}

	fun readLanguagePopupState(): Flow<Boolean> {
		return dataStore.readLanguagePopupState()
	}

	suspend fun saveGuidedOnboardingStep(step: String) {
		withContext(ioDispatcher) {
			dataStore.saveGuidedOnboardingStep(step)
		}
	}

	fun readGuidedOnboardingStep(): Flow<String> {
		return dataStore.readGuidedOnboardingStep()
	}

	suspend fun saveSettingsTooltipStep(step: Int) {
		withContext(ioDispatcher) {
			dataStore.saveSettingsTooltipStep(step)
		}
	}

	fun readSettingsTooltipStep(): Flow<Int> {
		return dataStore.readSettingsTooltipStep()
	}

	suspend fun saveLastSuccessfulDataRefreshAt(timestampMillis: Long) {
		withContext(ioDispatcher) {
			dataStore.saveLastSuccessfulDataRefreshAt(timestampMillis)
		}
	}

	fun readLastSuccessfulDataRefreshAt(): Flow<Long> {
		return dataStore.readLastSuccessfulDataRefreshAt()
	}
}
