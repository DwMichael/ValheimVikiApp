package com.rabbitv.valheimviki.presentation.detail.weapon.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class WeaponUiState(
	val weapon: Weapon? = null,
	val materials: List<MaterialUpgrade> = emptyList(),
	val foodAsMaterials: List<FoodAsMaterialUpgrade> = emptyList(),
	val craftingObjects: CraftingObject? = null,
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)
