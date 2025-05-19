package com.rabbitv.valheimviki.presentation.weapons.model

import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption

enum class WeaponSegmentOption(
    override val label: String,
    override val value: WeaponSubCategory
) : SegmentedOption<WeaponSubCategory> {
    MELEE("Melee", WeaponSubCategory.MELEE_WEAPON),
    RANGED("Ranged", WeaponSubCategory.RANGED_WEAPON),
    MAGIC("Magic", WeaponSubCategory.MAGIC_WEAPON),
    AMMO("Ammo", WeaponSubCategory.AMMO)
}