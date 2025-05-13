package com.rabbitv.valheimviki.presentation.weapons

import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class WeaponListUiState(
    val weaponList: List<Weapon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
