package com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory

class GetWeaponsBySubCategoryUseCase {
    fun invoke(weapons: List<Weapon>, subCategory: WeaponSubCategory): List<Weapon> {
        return weapons.filter { it.subCategory == subCategory.toString() }
    }
}