package com.rabbitv.valheimviki.domain.use_cases.datastore.data_language_provider

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataLanguageProvider @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
	operator fun invoke(): Flow<String> {
		return dataStoreRepository.dataLanguageProvider()
	}
}
