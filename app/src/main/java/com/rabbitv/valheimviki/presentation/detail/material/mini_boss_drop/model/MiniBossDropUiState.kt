package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material

data class MiniBossDropUiState(
	val material: Material? = null,
	val miniBoss: List<Creature> = emptyList(),
	val npc: List<Creature> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
