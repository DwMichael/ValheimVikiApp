package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.presentation.detail.creature.components.DropItem

data class AggressiveCreatureDetailUiState(
    val aggressiveCreature: AggressiveCreature? = null,
    val biome: Biome? = null,
    val dropItems: List<DropItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)