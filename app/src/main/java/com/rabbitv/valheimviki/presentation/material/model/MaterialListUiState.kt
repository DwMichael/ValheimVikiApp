package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType

data class MaterialListUiState(
    val materialsList: List<Material> = emptyList(),
    val selectedSubCategory: MaterialSubCategory? = null,
    val selectedSubType: MaterialSubType? = null,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
