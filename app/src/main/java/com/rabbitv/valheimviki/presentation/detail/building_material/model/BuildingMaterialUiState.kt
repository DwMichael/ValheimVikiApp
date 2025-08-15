package com.rabbitv.valheimviki.presentation.detail.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class BuildingMaterialUiState(
	val buildingMaterial: BuildingMaterial? = null,
	val materials: UIState<List<RequiredMaterial>> = UIState.Loading,
	val foods: UIState<List<RequiredFood>> = UIState.Loading,
	val craftingStation: UIState<List<CraftingObject>> = UIState.Loading,
	val isFavorite: Boolean = false,
)
