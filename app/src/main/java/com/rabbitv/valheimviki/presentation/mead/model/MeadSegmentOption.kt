package com.rabbitv.valheimviki.presentation.mead.model

import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class MeadSegmentOption(
    override val labelRes: Int,
    override val value: MeadSubCategory
) : SegmentedOption<MeadSubCategory> {
    MEAD_BASES(com.rabbitv.valheimviki.R.string.segment_mead_bases, MeadSubCategory.MEAD_BASE),
    POTIONS(com.rabbitv.valheimviki.R.string.segment_potions, MeadSubCategory.POTION),
}