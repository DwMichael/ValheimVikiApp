package com.rabbitv.valheimviki.presentation.detail.armor.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade

data class ArmorDetailUiState(
	val armor: Armor? = null,
	val materials: UIState<List<MaterialUpgrade>> = UIState.Loading,
	val foodAsMaterials: UIState<List<FoodAsMaterialUpgrade>> = UIState.Loading,
	val craftingObject: UIState<CraftingObject?> = UIState.Loading,
	val isFavorite: Boolean = false,
)
