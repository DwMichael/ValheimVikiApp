package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.presentation.detail.creature.components.MaterialDrop

data class AggressiveCreatureDetailUiState(
    val aggressiveCreature: AggressiveCreature? = null,
    val biome: Biome? = null,
    val materialDrops: List<MaterialDrop> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)