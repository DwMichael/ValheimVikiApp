package com.rabbitv.valheimviki.presentation.detail.point_of_interest.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class PointOfInterestUiState(
	val pointOfInterest: PointOfInterest? = null,
	val relatedBiomes: List<Biome> = emptyList(),
	val relatedCreatures: List<Creature> = emptyList(),
	val relatedMaterials: List<Material> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null
)
