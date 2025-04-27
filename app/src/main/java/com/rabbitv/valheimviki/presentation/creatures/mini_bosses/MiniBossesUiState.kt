package com.rabbitv.valheimviki.presentation.creatures.mini_bosses

import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss

data class MiniBossesUiState(
    val miniBosses: List<MiniBoss> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)
