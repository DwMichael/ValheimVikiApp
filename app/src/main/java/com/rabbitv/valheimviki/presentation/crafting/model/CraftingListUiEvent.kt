package com.rabbitv.valheimviki.presentation.crafting.model


import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory


sealed class CraftingListUiEvent {
	data class ChipSelected(val chip: CraftingSubCategory?) : CraftingListUiEvent()
}