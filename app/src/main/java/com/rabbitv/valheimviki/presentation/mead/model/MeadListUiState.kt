package com.rabbitv.valheimviki.presentation.mead.model

import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory

data class MeadListUiState(
    val meadList: List<Mead> = emptyList(),
    val selectedSubCategory: MeadSubCategory = MeadSubCategory.MEAD_BASE,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
