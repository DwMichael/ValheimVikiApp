package com.rabbitv.valheimviki.domain.use_cases.datastore.get_guided_onboarding_step

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadGuidedOnboardingStep @Inject constructor(
	private val dataStoreRepository: DataStoreRepository
) {
	operator fun invoke(): Flow<GuidedOnboardingStep> =
		dataStoreRepository.readGuidedOnboardingStep()
			.map(GuidedOnboardingStep::fromStored)
}
