package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model

import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class MiniBossDetailUiState(
	val miniBoss: MiniBoss? = null,
	val primarySpawn: PointOfInterest? = null,
	val dropItems: UIState<List<Material>> = UIState.Loading,
	val isFavorite: Boolean = false
)