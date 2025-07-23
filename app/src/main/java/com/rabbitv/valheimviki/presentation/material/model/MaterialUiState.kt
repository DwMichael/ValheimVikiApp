package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class MaterialUiState(
	val selectedCategory: MaterialSubCategory? = null,
	val selectedChip: MaterialSubType? = null,
	val materialsUiState: UIState<List<Material>>
)
