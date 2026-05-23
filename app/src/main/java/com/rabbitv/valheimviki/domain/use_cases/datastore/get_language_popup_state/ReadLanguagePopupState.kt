package com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_popup_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadLanguagePopupState @Inject constructor(
    private val repository: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.readLanguagePopupState()
    }
}
