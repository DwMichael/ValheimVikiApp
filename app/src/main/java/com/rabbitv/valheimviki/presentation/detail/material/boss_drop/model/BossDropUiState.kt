package com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model

import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material

data class BossDropUiState(
	val material: Material? = null,
	val boss: MainBoss? = null,
	val isLoading: Boolean = false,
	val error: String? = null,
)