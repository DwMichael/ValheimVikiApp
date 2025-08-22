package com.rabbitv.valheimviki.presentation.detail.material.offerings.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class OfferingUiState(
	val material: Material? = null,
	val passive: UIState<List<PassiveCreature>> = UIState.Loading,
	val aggressive: UIState<List<AggressiveCreature>> = UIState.Loading,
	val pointsOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val altars: UIState<List<PointOfInterest>> = UIState.Loading,
	val craftingStation: UIState<List<CraftingObject>> = UIState.Loading,
	val isFavorite: Boolean = false
)
