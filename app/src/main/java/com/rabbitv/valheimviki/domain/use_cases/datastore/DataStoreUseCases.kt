package com.rabbitv.valheimviki.domain.use_cases.datastore

import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState


data class DataStoreUseCases(
    val readOnBoardingUseCase: ReadOnBoardingState,
    val saveOnBoardingState: SaveOnBoardingState,
    val languageProvider: LanguageProvider,
    val saveLanguageState: SaveLanguageState
)