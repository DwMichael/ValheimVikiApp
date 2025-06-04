package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.presentation.detail.creature.components.MaterialDrop

data class PassiveCreatureDetailUiState(
    val passiveCreature: PassiveCreature? = null,
    val biome: Biome? = null,
    val materialDrops: List<MaterialDrop> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)