package com.rabbitv.valheimviki.presentation.creatures.mob_list

import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature

data class MobListUiState(
    val aggressiveCreatures: List<AggressiveCreature> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)
