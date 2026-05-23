package com.rabbitv.valheimviki.domain.use_cases.datastore.save_language_popup_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveLanguagePopupState @Inject constructor(
    private val repository: DataStoreRepository
) {
    suspend operator fun invoke(shown: Boolean) {
        repository.saveLanguagePopupState(shown)
    }
}
