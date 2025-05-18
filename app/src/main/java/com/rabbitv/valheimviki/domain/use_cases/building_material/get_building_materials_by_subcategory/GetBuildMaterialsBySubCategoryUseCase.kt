package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory

import com.rabbitv.valheimviki.domain.exceptions.MaterialsFetchLocalException
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject

class GetBuildMaterialsBySubCategoryUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(subCategory: BuildingMaterialSubCategory): List<BuildingMaterial> {

        val buildingMaterials =
            buildingMaterialRepository.getBuildingMaterialsBySubCategory(subCategory = subCategory.toString())
        if (buildingMaterials.isNotEmpty()) {
            return buildingMaterials
        } else {
            throw MaterialsFetchLocalException("No building Material found for subCategory $subCategory")
        }
    }
}