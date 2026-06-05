package com.rabbitv.valheimviki.domain.model.onboarding

enum class GuidedOnboardingStep {
	INFO_CARD,
	HOME_SETTINGS,
	SETTINGS_LANGUAGE,
	SETTINGS_FANDOM,
	SETTINGS_DONATE,
	DONE;

	val isActive: Boolean
		get() = this != DONE

	val isSettingsStep: Boolean
		get() = this == SETTINGS_LANGUAGE ||
			this == SETTINGS_FANDOM ||
			this == SETTINGS_DONATE

	companion object {
		fun fromStored(value: String?): GuidedOnboardingStep =
			values().firstOrNull { it.name == value } ?: INFO_CARD
	}
}
