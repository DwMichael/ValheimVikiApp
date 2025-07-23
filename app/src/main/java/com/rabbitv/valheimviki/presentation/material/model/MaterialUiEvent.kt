package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType

sealed class MaterialUiEvent {
	data class CategorySelected(val category: MaterialSubCategory?) :
		MaterialUiEvent()

	data class ChipSelected(val chip: MaterialSubType?) : MaterialUiEvent()
}