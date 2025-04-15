package com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id

import com.rabbitv.valheimviki.domain.exceptions.MaterialsByIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class GetMaterialByIdUseCase@Inject constructor(
    private val materialRepository: MaterialRepository
){
    operator fun invoke(id:String):Material? {
        return materialRepository.getMaterialById(id) ?: throw MaterialsByIdFetchLocalException("Material with ID $id not found")
    }
}