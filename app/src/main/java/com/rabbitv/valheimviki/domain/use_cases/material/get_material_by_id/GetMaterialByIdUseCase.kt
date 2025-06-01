package com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetMaterialByIdUseCase @Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(id: String): Flow<Material?> {
        return materialRepository.getMaterialById(id).flowOn(Dispatchers.IO)
    }
}