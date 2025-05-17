package com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_type_use_case

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType

class GetWeaponsBySubTypeUseCase {
   operator fun invoke(weapons: List<Weapon>, subType: WeaponSubType): List<Weapon> {
        return weapons.filter { it.subCategory == subType.toString() }
    }
}