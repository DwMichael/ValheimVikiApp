package com.rabbitv.valheimviki.presentation.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType

data class BuildingMaterialListUiState(
    val buildingMaterialList: List<BuildingMaterial> = emptyList(),
    val selectedSubCategory: BuildingMaterialSubCategory? = null,
    val selectedSubType: BuildingMaterialSubType? = null,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
