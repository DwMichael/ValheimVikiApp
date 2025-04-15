package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class GetMaterialsBySubCategoryAndSubTypeUseCase@Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(subCategory: MaterialSubCategory, subType: MaterialSubType): List<Material> {
        return materialRepository.getMaterialsBySubCategoryAndSubType(subType= subType.toString(), subCategory = subCategory.toString())
    }
}