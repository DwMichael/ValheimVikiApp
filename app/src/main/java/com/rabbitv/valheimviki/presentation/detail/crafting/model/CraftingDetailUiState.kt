package com.rabbitv.valheimviki.presentation.detail.crafting.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class CraftingDetailUiState(
	val craftingObject: CraftingObject? = null,
	val craftingUpgraderObjects: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingFoodProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingMeadProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingMaterialToBuild: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingMaterialRequired: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingMaterialProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingWeaponProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingArmorProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingToolProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val craftingBuildingMaterialProducts: UIState<List<CraftingProducts>> = UIState.Loading,
	val isFavorite: Boolean = false,
)
