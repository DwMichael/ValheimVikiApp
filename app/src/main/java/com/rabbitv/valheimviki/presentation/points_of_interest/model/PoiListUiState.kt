package com.rabbitv.valheimviki.presentation.points_of_interest.model

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory

data class PoiListUiState(
    val poiList: List<PointOfInterest> = emptyList(),
    val selectedSubCategory: PointOfInterestSubCategory = PointOfInterestSubCategory.FORSAKEN_ALTAR,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
