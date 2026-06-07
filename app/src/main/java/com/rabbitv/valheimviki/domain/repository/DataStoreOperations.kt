package com.rabbitv.valheimviki.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {
    suspend fun saveOnBoardingState(completed: Boolean)
    fun readOnBoardingState(): Flow<Boolean>
    suspend fun saveLanguage(language: String)
    fun languageProvider(): Flow<String>
    suspend fun saveDataLanguage(language: String)
    fun dataLanguageProvider(): Flow<String>
    suspend fun saveLanguagePopupState(shown: Boolean)
    fun readLanguagePopupState(): Flow<Boolean>
    suspend fun saveGuidedOnboardingStep(step: String)
    fun readGuidedOnboardingStep(): Flow<String>
    suspend fun saveSettingsTooltipStep(step: Int)
    fun readSettingsTooltipStep(): Flow<Int>
    suspend fun saveLastSuccessfulDataRefreshAt(timestampMillis: Long)
    fun readLastSuccessfulDataRefreshAt(): Flow<Long>
}
