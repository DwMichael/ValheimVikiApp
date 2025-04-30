package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature

data class AggressiveCreatureDetailUiState(
    val aggressiveCreature : AggressiveCreature?  = null,
    val biome: Biome? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
