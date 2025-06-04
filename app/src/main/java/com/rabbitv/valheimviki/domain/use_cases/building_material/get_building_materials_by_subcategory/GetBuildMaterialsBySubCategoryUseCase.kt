package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBuildMaterialsBySubCategoryUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(subCategory: BuildingMaterialSubCategory): Flow<List<BuildingMaterial>> =
        buildingMaterialRepository.getBuildingMaterialsBySubCategory(subCategory = subCategory.toString())
            .flowOn(
                Dispatchers.IO
            )


}