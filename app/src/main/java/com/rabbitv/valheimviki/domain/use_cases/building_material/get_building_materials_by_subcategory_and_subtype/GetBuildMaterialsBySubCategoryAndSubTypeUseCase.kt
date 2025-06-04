package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory_and_subtype

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBuildMaterialsBySubCategoryAndSubTypeUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(
        subCategory: BuildingMaterialSubCategory,
        subType: BuildingMaterialSubType
    ): Flow<List<BuildingMaterial>> =
        buildingMaterialRepository.getBuildingMaterialsBySubCategoryAndSubType(
            subType = subType.toString(),
            subCategory = subCategory.toString()
        )

}