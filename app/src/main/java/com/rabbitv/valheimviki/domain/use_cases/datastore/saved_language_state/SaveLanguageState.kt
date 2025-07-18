package com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveLanguageState @Inject constructor(
	private val dataStoreRepository: DataStoreRepository
) {
	suspend operator fun invoke(lang: String) {
		return dataStoreRepository.saveLanguage(lang)
	}
}