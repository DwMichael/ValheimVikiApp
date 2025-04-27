package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class MiniBossDetailUiState(
    val miniBoss: MiniBoss? = null,
    val primarySpawn: PointOfInterest? = null,
    val dropItems: List<Material> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
