package com.rabbitv.valheimviki.presentation.detail.material.gemstones.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class GemstoneDetailUiState(
	val material: Material? = null,
	val pointsOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val isFavorite: Boolean = false
)
