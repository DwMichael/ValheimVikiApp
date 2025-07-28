package com.rabbitv.valheimviki.presentation.weapons.model

import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType

sealed class WeaponUiEvent {
	data class CategorySelected(val category: WeaponSubCategory) : WeaponUiEvent()
	data class ChipSelected(val chip: WeaponSubType?) : WeaponUiEvent()
}