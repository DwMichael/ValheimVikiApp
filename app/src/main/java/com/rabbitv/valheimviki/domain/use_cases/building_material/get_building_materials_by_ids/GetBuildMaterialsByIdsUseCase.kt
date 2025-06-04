package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_ids

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBuildMaterialsByIdsUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(ids: List<String>): Flow<List<BuildingMaterial>> =
        buildingMaterialRepository.getBuildingMaterialsByIds(ids).flowOn(Dispatchers.IO)
}