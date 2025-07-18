package com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadOnBoardingState @Inject constructor(
	private val dataStoreRepository: DataStoreRepository
) {
	operator fun invoke(): Flow<Boolean> {
		return dataStoreRepository.readOnBoardingState()
	}
}