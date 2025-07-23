package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMaterialsByIdsUseCase @Inject constructor(
	private val materialRepository: MaterialRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<Material>> {
		return materialRepository.getMaterialsByIds(ids)
	}
}