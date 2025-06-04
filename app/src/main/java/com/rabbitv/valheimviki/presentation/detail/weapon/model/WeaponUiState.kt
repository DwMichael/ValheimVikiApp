package com.rabbitv.valheimviki.presentation.detail.weapon.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class WeaponUiState(
    val weapon: Weapon? = null,
    val materials: List<Material> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
