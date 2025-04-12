package com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetLanguageState(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<String> {
        return dataStoreRepository.languageProvider()
    }
}