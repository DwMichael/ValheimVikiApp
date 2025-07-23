package com.rabbitv.valheimviki.presentation.crafting.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState


data class CraftingListUiState(
	val selectedChip: CraftingSubCategory? = null,
	val craftingListUiState: UIState<List<CraftingObject>>
)