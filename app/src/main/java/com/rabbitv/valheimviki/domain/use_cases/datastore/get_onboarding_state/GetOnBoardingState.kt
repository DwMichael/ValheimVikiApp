package com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOnBoardingState @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(
        repository: DataStoreRepository
    ): Flow<Boolean> {
        return repository.readOnBoardingState()
    }
}