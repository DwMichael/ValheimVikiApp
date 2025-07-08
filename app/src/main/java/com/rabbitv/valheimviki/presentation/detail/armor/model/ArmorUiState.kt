package com.rabbitv.valheimviki.presentation.detail.armor.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade

data class ArmorUiState(
    val armor: Armor? = null,
    val materials: List<MaterialUpgrade> = emptyList(),
    val craftingObject: CraftingObject? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
