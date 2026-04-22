package com.rabbitv.valheimviki.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {
    suspend fun saveOnBoardingState(completed: Boolean)
    fun readOnBoardingState(): Flow<Boolean>
    suspend fun saveLanguage(language: String)
    fun languageProvider(): Flow<String>
    suspend fun saveLanguagePopupState(shown: Boolean)
    fun readLanguagePopupState(): Flow<Boolean>
}