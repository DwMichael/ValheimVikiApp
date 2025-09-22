package com.rabbitv.valheimviki.presentation.detail.trinket.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade

data class TrinketDetailUiState(
	val trinket: Trinket? = null,
	val materials: UIState<List<MaterialUpgrade>> = UIState.Loading,
	val craftingObject: UIState<CraftingObject?> = UIState.Loading,
	val isFavorite: Boolean = false,
)
