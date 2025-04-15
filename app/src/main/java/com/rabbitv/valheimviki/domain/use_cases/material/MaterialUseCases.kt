package com.rabbitv.valheimviki.domain.use_cases.material

import com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials.GetLocalMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id.GetMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory.GetMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype.GetMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.insert_materials.InsertMaterialsUseCase

data class MaterialUseCases(
    val getLocalMaterials: GetLocalMaterialsUseCase,
    val getMaterialsByIds: GetMaterialsByIdsUseCase,
    val getMaterialById: GetMaterialByIdUseCase,
    val getMaterialsBySubCategory: GetMaterialsBySubCategoryUseCase,
    val getMaterialsBySubCategoryAndSubType: GetMaterialsBySubCategoryAndSubTypeUseCase,
    val insertMaterials: InsertMaterialsUseCase,
)
