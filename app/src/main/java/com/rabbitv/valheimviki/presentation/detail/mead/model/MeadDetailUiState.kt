package com.rabbitv.valheimviki.presentation.detail.mead.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData

data class MeadDetailUiState(
	val mead: Mead? = null,
	val craftingCookingStation: UIState<CraftingObject?> = UIState.Loading,
	val recipeItems:UIState< List<RecipeMaterialData<ItemData>>> = UIState.Loading,
	val isFavorite: Boolean = false,
)
