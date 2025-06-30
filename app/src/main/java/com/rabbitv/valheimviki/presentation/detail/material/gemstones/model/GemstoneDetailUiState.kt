package com.rabbitv.valheimviki.presentation.detail.material.gemstones.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class GemstoneDetailUiState(
	val material: Material? = null,
	val pointsOfInterest: List<PointOfInterest> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
