package com.rabbitv.valheimviki.domain.use_cases.datastore

import com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_state.GetLanguageState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState


data class DataStoreUseCases(
    val readOnBoardingUseCase: ReadOnBoardingState,
    val saveOnBoardingState: SaveOnBoardingState,
    val languageProvider: GetLanguageState,
    val saveLanguageState: SaveLanguageState
)