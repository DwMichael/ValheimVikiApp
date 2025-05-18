package com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_material_by_id

import com.rabbitv.valheimviki.domain.exceptions.MaterialsByIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import jakarta.inject.Inject


class GetBuildMaterialByIdUseCase @Inject constructor(
    private val buildingMaterialRepository: BuildingMaterialRepository
) {
    operator fun invoke(id: String): BuildingMaterial? {
        return buildingMaterialRepository.getBuildingMaterialById(id)
            ?: throw MaterialsByIdFetchLocalException("Building Material with ID $id not found")
    }
}