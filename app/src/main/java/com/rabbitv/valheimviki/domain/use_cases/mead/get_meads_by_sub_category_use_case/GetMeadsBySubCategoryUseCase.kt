package com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory

class GetMeadsBySubCategoryUseCase {
    operator fun invoke(armors: List<Armor>, subCategory: ArmorSubCategory): List<Armor> {
        return armors.filter { it.subCategory == subCategory.toString() }
    }
}