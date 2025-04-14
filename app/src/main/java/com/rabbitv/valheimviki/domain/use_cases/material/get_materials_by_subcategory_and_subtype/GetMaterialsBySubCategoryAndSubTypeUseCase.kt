package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class GetMaterialsBySubCategoryAndSubTypeUseCase@Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(subCategory: String, subType: String): List<Material> {
        return materialRepository.getMaterialsBySubCategoryAndSubType(subType= subType, subCategory = subCategory)
    }
}