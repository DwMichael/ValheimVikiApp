package com.rabbitv.valheimviki.presentation.mead.model

import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState


data class MeadUiSate(
	val meadState: UIState<List<Mead>> = UIState.Loading,
	val selectedCategory: MeadSubCategory = MeadSubCategory.MEAD_BASE,
)