package com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model

import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest


data class MobDropUiState(
	val material: Material? = null,
	val passive: List<PassiveCreature> = emptyList(),
	val aggressive: List<AggressiveCreature> = emptyList(),
	val pointsOfInterest: List<PointOfInterest> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)