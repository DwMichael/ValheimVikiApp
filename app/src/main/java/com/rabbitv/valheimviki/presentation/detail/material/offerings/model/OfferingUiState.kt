package com.rabbitv.valheimviki.presentation.detail.material.offerings.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class OfferingUiState(
	val material: Material? = null,
	val passive: List<PassiveCreature> = emptyList(),
	val aggressive: List<AggressiveCreature> = emptyList(),
	val pointsOfInterest: List<PointOfInterest> = emptyList(),
	val altars: List<PointOfInterest> = emptyList(),
	val craftingStation: List<CraftingObject> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
