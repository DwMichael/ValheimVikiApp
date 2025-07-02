package com.rabbitv.valheimviki.presentation.detail.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject

data class BuildingMaterialUiState(
	val buildingMaterial: BuildingMaterial? = null,
	val materials: List<RequiredMaterial> = emptyList(),
	val foods: List<RequiredFood> = emptyList(),
	val craftingStation: List<CraftingObject> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null
)
