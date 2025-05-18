package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_ids

import com.rabbitv.valheimviki.domain.exceptions.MaterialsByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject

class GetBuildMaterialsByIdsUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(ids: List<String>): List<BuildingMaterial> {

        return try {
            val materials = buildingMaterialRepository.getBuildingMaterialsByIds(ids)
            if (materials.isNotEmpty()) {
                materials.sortedBy { it.order }
            } else {
                throw MaterialsByIdsFetchLocalException("No building Material found with ids $ids")
            }
        } catch (e: Exception) {
            throw MaterialsByIdsFetchLocalException("Error fetching from Room materials by ids : ${e.message}")
        }
    }
}