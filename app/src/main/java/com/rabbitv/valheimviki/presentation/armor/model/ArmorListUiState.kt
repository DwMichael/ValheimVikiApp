package com.rabbitv.valheimviki.presentation.armor.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory

data class ArmorListUiState(
    val armorList: List<Armor> = emptyList(),
    val selectedChip: ArmorSubCategory? = null,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)