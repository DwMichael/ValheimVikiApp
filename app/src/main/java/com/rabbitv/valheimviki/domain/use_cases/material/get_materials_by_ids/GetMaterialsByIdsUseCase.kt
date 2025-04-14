package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids

import com.rabbitv.valheimviki.domain.exceptions.MaterialsByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class GetMaterialsByIdsUseCase @Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(ids: List<String>): List<Material> {

        return try {
            val materials = materialRepository.getMaterialsByIds(ids)
            if (materials.isNotEmpty()) {
                materials.sortedBy { it.order }
            }else
            {
                throw MaterialsByIdsFetchLocalException("No materials found with ids $ids")
            }
        } catch (e: Exception) {
            throw MaterialsByIdsFetchLocalException("Error fetching from Room materials by ids : ${e.message}")
        }
    }
}