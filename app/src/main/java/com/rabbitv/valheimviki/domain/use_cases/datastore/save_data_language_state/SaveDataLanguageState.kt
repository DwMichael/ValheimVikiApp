package com.rabbitv.valheimviki.domain.use_cases.datastore.save_data_language_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveDataLanguageState @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
	suspend operator fun invoke(lang: String) {
		return dataStoreRepository.saveDataLanguage(lang)
	}
}
