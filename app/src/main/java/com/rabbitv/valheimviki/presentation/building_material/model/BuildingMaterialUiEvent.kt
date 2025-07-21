package com.rabbitv.valheimviki.presentation.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType

sealed class BuildingMaterialUiEvent {
	data class CategorySelected(val category: BuildingMaterialSubCategory?) :
		BuildingMaterialUiEvent()

	data class ChipSelected(val chip: BuildingMaterialSubType?) : BuildingMaterialUiEvent()
}