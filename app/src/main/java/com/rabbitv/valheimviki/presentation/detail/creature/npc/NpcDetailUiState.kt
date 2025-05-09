package com.rabbitv.valheimviki.presentation.detail.creature.npc

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material

data class NpcDetailUiState(
    val npc: NPC? = null,
    val biome: Biome? = null,
    val shopItems: List<Material> = emptyList(),
    val shopSellItems: List<Material> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
