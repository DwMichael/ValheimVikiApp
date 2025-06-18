package com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory

class GetCraftingObjectsBySubCategoryUseCase {
    operator fun invoke(
        craftingObjects: List<CraftingObject>,
        subCategory: CraftingSubCategory
    ): List<CraftingObject> {
        return craftingObjects.filter { it.subCategory == subCategory.toString() }
    }
}