package com.rabbitv.valheimviki.presentation.weapons.model

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType

data class WeaponListUiState(
    val weaponList: List<Weapon> = emptyList(),
    val selectedCategory: WeaponSubCategory = WeaponSubCategory.MELEE_WEAPON,
    val selectedChip: WeaponSubType? = null,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)