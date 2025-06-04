package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_material_by_id

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


class GetBuildMaterialByIdUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(id: String): Flow<BuildingMaterial?> {
        return buildingMaterialRepository.getBuildingMaterialById(id).flowOn(Dispatchers.IO)
    }
}