package com.rabbitv.valheimviki.presentation.detail.material.valuable.model

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class ValuableMaterialUiState(
	val material: Material? = null,
	val pointsOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val npc: UIState<List<NPC>> = UIState.Loading,
	val creatures: UIState<List<Creature>> = UIState.Loading,
	val isFavorite: Boolean = false
)
