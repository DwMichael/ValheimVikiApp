package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model

import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material

data class MiniBossDropUiState(
	val material: Material? = null,
	val miniBoss: MiniBoss? = null,
	val npc: NPC? = null,
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)
