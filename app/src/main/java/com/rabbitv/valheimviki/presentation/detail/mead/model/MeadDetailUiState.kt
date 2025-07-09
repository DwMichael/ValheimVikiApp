package com.rabbitv.valheimviki.presentation.detail.mead.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeFoodData
import com.rabbitv.valheimviki.presentation.detail.food.model.RecipeMaterialData

data class MeadDetailUiState(
	val mead: Mead? = null,
	val craftingCookingStation: CraftingObject? = null,
	val foodForRecipe: List<RecipeFoodData> = emptyList(),
	val meadForRecipe: List<RecipeMeadData> = emptyList(),
	val materialsForRecipe: List<RecipeMaterialData> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)
