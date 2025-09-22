package com.rabbitv.valheimviki.presentation.trinkets.model

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class TrinketUiState(
	val trinketsUiState: UIState<List<Trinket>>
)
