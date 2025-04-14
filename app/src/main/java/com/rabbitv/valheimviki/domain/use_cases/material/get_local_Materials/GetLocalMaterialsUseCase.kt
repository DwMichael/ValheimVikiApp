package com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalMaterialsUseCase@Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke():Flow<List<Material>> {
        return materialRepository.getLocalMaterials()
    }
}