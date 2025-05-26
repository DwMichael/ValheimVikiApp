package com.rabbitv.valheimviki.presentation.points_of_interest.model

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class PoiSegmentOption(
    override val label: String,
    override val value: PointOfInterestSubCategory
) : SegmentedOption<PointOfInterestSubCategory> {
    FORSAKEN_ALTAR("ALTARS", PointOfInterestSubCategory.FORSAKEN_ALTAR),
    STRUCTURE("STRUCTURES", PointOfInterestSubCategory.STRUCTURE),
}