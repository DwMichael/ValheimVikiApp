package com.rabbitv.valheimviki.presentation.points_of_interest.model

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class PoiListUiState(
	val poiListState: UIState<List<PointOfInterest>> = UIState.Loading,
	val selectedSubCategory: PointOfInterestSubCategory = PointOfInterestSubCategory.FORSAKEN_ALTAR,
)
