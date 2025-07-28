package com.rabbitv.valheimviki.presentation.points_of_interest.model

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory

sealed class PoiListUiEvent {
	data class CategorySelected(val category: PointOfInterestSubCategory) : PoiListUiEvent()
}