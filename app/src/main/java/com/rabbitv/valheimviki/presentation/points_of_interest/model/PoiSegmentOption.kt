package com.rabbitv.valheimviki.presentation.points_of_interest.model

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class PoiSegmentOption(
    override val labelRes: Int,
    override val value: PointOfInterestSubCategory
) : SegmentedOption<PointOfInterestSubCategory> {
    FORSAKEN_ALTAR(com.rabbitv.valheimviki.R.string.segment_altars, PointOfInterestSubCategory.FORSAKEN_ALTAR),
    STRUCTURE(com.rabbitv.valheimviki.R.string.segment_structures, PointOfInterestSubCategory.STRUCTURE),
}