package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subCategory_and_ids

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMaterialsBySubCategoryIdsUseCase @Inject constructor(
	private val materialRepository: MaterialRepository
) {
	operator fun invoke(subCategory: String,ids: List<String>): Flow<List<Material>> {
		return materialRepository.getMaterialsByCategoryAndIds(subCategory,ids)
	}
}