package com.rabbitv.valheimviki.presentation.detail.material.valuable.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class ValuableMaterialUiState(
	val material: Material? = null,
	val biome: Biome? = null,
	val pointsOfInterest: List<PointOfInterest> = emptyList(),
	val npc: List<NPC> = emptyList(),
	val creatures: List<Creature> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)
