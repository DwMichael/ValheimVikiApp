package com.rabbitv.valheimviki.domain.use_cases.datastore.save_guided_onboarding_step

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep
import javax.inject.Inject

class SaveGuidedOnboardingStep @Inject constructor(
	private val dataStoreRepository: DataStoreRepository
) {
	suspend operator fun invoke(step: GuidedOnboardingStep) {
		dataStoreRepository.saveGuidedOnboardingStep(step.name)
	}
}
