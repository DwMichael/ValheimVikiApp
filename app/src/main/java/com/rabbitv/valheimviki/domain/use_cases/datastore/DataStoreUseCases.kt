package com.rabbitv.valheimviki.domain.use_cases.datastore

import com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_popup_state.ReadLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_settings_tooltip_state.ReadSettingsTooltipState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_language_popup_state.SaveLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_settings_tooltip_state.SaveSettingsTooltipState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class DataStoreUseCases @Inject constructor(
	val readOnBoardingUseCase: ReadOnBoardingState,
	val saveOnBoardingState: SaveOnBoardingState,
	val languageProvider: LanguageProvider,
	val saveLanguageState: SaveLanguageState,
	val readLanguagePopupState: ReadLanguagePopupState,
	val saveLanguagePopupState: SaveLanguagePopupState,
	val readSettingsTooltipState: ReadSettingsTooltipState,
	val saveSettingsTooltipState: SaveSettingsTooltipState
)