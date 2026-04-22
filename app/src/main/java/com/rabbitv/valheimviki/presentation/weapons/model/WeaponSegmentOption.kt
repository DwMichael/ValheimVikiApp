package com.rabbitv.valheimviki.presentation.weapons.model

import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption

enum class WeaponSegmentOption(
    override val labelRes: Int,
    override val value: WeaponSubCategory
) : SegmentedOption<WeaponSubCategory> {
    MELEE(com.rabbitv.valheimviki.R.string.segment_melee, WeaponSubCategory.MELEE_WEAPON),
    RANGED(com.rabbitv.valheimviki.R.string.segment_ranged, WeaponSubCategory.RANGED_WEAPON),
    MAGIC(com.rabbitv.valheimviki.R.string.segment_magic, WeaponSubCategory.MAGIC_WEAPON),
    AMMO(com.rabbitv.valheimviki.R.string.segment_ammo, WeaponSubCategory.AMMO)
}