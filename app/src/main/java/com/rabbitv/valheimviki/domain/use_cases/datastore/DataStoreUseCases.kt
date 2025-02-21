package com.rabbitv.valheimviki.domain.use_cases.datastore

import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState


data class DataStoreUseCases(
    val readOnBoardingUseCase: ReadOnBoardingState,
    val saveOnBoardingState: SaveOnBoardingState
)