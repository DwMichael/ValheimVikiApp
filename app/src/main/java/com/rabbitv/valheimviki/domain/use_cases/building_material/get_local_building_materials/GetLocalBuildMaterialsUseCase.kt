package com.rabbitv.valheimviki.domain.use_cases.building_material.get_local_building_materials

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalBuildMaterialsUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(): Flow<List<BuildingMaterial>> {
        return buildingMaterialRepository.getLocalBuildingMaterials()
    }
}