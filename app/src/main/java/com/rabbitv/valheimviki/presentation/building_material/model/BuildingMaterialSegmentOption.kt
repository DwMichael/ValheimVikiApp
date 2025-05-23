package com.rabbitv.valheimviki.presentation.building_material.model

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption


enum class BuildingMaterialSegmentOption(
    override val label: String,
    override val value: BuildingMaterialSubCategory
) : GridCategoryOption<BuildingMaterialSubCategory> {
    WOOD("WOOD", BuildingMaterialSubCategory.WOOD),
    CORE_WOOD("CORE WOOD", BuildingMaterialSubCategory.CORE_WOOD),
    RESOURCE("RESOURCES", BuildingMaterialSubCategory.RESOURCE),
    STONE_AND_METAL("STONE AND METALS", BuildingMaterialSubCategory.STONE_AND_METAL),
    TRANSPORT("TRANSPORTS", BuildingMaterialSubCategory.TRANSPORT),
    DEFENSE("DEFENSE", BuildingMaterialSubCategory.DEFENSE),
    FURNITURE("FURNITURE", BuildingMaterialSubCategory.FURNITURE),
    SIEGE("SIEGE", BuildingMaterialSubCategory.SIEGE),
    LIGHT_SOURCE("LIGHT SOURCES", BuildingMaterialSubCategory.LIGHT_SOURCE),
    DECORATIVE("DECORATIVE", BuildingMaterialSubCategory.DECORATIVE),
    ROOF("ROOFS", BuildingMaterialSubCategory.ROOF),
}