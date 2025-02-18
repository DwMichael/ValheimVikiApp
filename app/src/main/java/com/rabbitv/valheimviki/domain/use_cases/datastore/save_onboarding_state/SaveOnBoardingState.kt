package com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveOnBoardingState @Inject constructor(
    private val repository: DataStoreRepository
) {
    suspend operator fun invoke(completed: Boolean) {
        repository.saveOnBoardingState(completed = completed)
    }
}