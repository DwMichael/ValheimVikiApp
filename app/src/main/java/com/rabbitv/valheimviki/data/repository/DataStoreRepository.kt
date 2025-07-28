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
}