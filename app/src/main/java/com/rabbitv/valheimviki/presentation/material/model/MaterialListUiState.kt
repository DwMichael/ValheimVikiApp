package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory

data class MaterialListUiState(
    val meadList: List<Mead> = emptyList(),
    val selectedSubCategory: MeadSubCategory = MeadSubCategory.MEAD_BASE,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
