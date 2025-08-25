package com.rabbitv.valheimviki.presentation.detail.weapon.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class WeaponUiState(
	val weapon: Weapon? = null,
	val materials: UIState< List<MaterialUpgrade>> = UIState.Loading,
	val foodAsMaterials:UIState< List<FoodAsMaterialUpgrade>> = UIState.Loading,
	val craftingObjects:UIState< CraftingObject?> = UIState.Loading,
	val isFavorite: Boolean = false,

	)
