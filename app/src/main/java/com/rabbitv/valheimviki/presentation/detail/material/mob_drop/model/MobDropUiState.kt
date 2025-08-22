package com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model

import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class MobDropUiState(
	val material: Material? = null,
	val passive: UIState<List<PassiveCreature>> = UIState.Loading,
	val aggressive: UIState<List<AggressiveCreature>> = UIState.Loading,
	val pointsOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val isFavorite: Boolean = false
)