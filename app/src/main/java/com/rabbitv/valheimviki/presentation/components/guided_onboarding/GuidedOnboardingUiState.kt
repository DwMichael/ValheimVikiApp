package com.rabbitv.valheimviki.presentation.components.guided_onboarding

import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep

data class GuidedOnboardingUiState(
	val initialized: Boolean = false,
	val step: GuidedOnboardingStep = GuidedOnboardingStep.INFO_CARD,
	val secondsRemaining: Int = 6
) {
	val isActive: Boolean
		get() = initialized && step.isActive

	val canAcceptInfoCard: Boolean
		get() = secondsRemaining <= 0
}
