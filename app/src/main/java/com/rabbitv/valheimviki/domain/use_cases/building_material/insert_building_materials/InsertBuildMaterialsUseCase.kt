package com.rabbitv.valheimviki.domain.use_cases.building_material.insert_building_materials

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject

class InsertBuildMaterialsUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    suspend operator fun invoke(buildingMaterials: List<BuildingMaterial>) {
        return buildingMaterialRepository.insertBuildingMaterial(buildingMaterials = buildingMaterials)
    }
}