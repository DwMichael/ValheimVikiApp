package com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory

import com.rabbitv.valheimviki.domain.exceptions.MaterialsFetchLocalException
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import jakarta.inject.Inject

class GetMaterialsBySubCategoryUseCase @Inject constructor(
    private val materialRepository: MaterialRepository
) {
    operator fun invoke(subCategory: MaterialSubCategory): List<Material> {

        val materials = materialRepository.getMaterialsBySubCategory(subCategory = subCategory.toString())
        if(materials.isNotEmpty()) {
            return materials
        }else {
            throw MaterialsFetchLocalException("No materials found for subCategory $subCategory")
        }
    }
}