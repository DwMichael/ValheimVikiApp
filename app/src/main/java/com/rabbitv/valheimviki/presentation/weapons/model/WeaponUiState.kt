package com.rabbitv.valheimviki.presentation.weapons.model

import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType

data class WeaponUiState(
	val selectedCategory: WeaponSubCategory = WeaponSubCategory.MELEE_WEAPON,
	val selectedChip: WeaponSubType? = null,
	val weaponUiState: UIState<List<Weapon>>
)