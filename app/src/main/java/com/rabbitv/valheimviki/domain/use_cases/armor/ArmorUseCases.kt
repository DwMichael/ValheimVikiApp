package com.rabbitv.valheimviki.domain.use_cases.armor

import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_sub_category_use_case.GetArmorsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case.GetLocalArmorsUseCase

data class ArmorUseCases(
    val getLocalArmorsUseCase: GetLocalArmorsUseCase,
    val getArmorsBySubCategoryUseCase: GetArmorsBySubCategoryUseCase,
)
