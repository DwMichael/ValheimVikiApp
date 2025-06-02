package com.rabbitv.valheimviki.presentation.detail.creature.npc.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class NpcDetailUiState(
    val npc: NPC? = null,
    val biome: Biome? = null,
    val shopItems: List<Material> = emptyList(),
    val shopSellItems: List<Material> = emptyList(),
    val hildirChests: List<Material> = emptyList(),
    val chestsLocation: List<PointOfInterest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)