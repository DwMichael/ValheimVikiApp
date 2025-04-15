package com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class LanguageProvider(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<String> {
        return dataStoreRepository.languageProvider()
    }
}