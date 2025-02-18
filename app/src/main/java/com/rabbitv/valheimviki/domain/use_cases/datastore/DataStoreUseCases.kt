package com.rabbitv.valheimviki.domain.use_cases.datastore

import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.GetOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState


data class DataStoreUseCases(
    val getOnBoardingState: GetOnBoardingState,
    val saveOnBoardingState: SaveOnBoardingState
)