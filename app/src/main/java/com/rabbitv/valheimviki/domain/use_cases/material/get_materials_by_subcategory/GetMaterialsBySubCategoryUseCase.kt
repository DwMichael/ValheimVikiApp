package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMaterialsBySubCategoryUseCase @Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(subCategory: MaterialSubCategory): Flow<List<Material>> {
        return materialRepository.getMaterialsBySubCategory(subCategory = subCategory.toString())
    }
}