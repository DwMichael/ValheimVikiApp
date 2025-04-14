package com.rabbitv.valheimviki.domain.use_cases.material.insert_materials

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class InsertMaterialsUseCase@Inject constructor(
    private val materialRepository: MaterialRepository
) {
    suspend operator fun invoke(materials: List<Material>){
        return materialRepository.insertMaterials(materials= materials)
    }
}