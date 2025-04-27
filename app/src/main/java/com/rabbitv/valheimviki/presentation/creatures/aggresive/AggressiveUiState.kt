package com.rabbitv.valheimviki.presentation.creatures.aggresive

import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature

data class AggressiveUiState(
    val creatures: List<AggressiveCreature> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)
