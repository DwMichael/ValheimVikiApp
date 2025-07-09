package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop

data class PassiveCreatureDetailUiState(
	val passiveCreature: PassiveCreature? = null,
	val biome: Biome? = null,
	val materialDrops: List<MaterialDrop> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)