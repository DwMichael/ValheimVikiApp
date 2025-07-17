package com.rabbitv.valheimviki.presentation.armor.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class ArmorListUiState(
	val selectedChip: ArmorSubCategory? = null,
	val armorsUiState: UIState<List<Armor>>
)
