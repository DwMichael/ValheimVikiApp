package com.rabbitv.valheimviki.presentation.detail.weapon.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class ArmorUiState(
    val armor: Armor? = null,
    val materials: List<MaterialUpgrade> = emptyList(),
    val craftingObjects: CraftingObject? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
