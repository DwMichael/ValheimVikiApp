package com.rabbitv.valheimviki.presentation.creatures.bosses

import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss

data class BossUiState(
    val bosses: List<MainBoss> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)