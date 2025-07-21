package com.rabbitv.valheimviki.presentation.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class BuildingMaterialUiState(
	val selectedCategory: BuildingMaterialSubCategory? = null,
	val selectedChip: BuildingMaterialSubType? = null,
	val materialsUiState: UIState<List<BuildingMaterial>>
)
