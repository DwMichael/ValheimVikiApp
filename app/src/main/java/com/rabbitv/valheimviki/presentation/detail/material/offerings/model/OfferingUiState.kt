package com.rabbitv.valheimviki.presentation.detail.material.offerings.model

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class OfferingUiState(
	val material: Material? = null,
	val boss: List<Creature> = emptyList(),
	val pointOfInterest: List<PointOfInterest> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
