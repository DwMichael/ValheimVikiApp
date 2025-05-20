package com.rabbitv.valheimviki.presentation.mead.model

import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class MeadSegmentOption(
    override val label: String,
    override val value: MeadSubCategory
) : SegmentedOption<MeadSubCategory> {
    MEAD_BASES("MEAD BASES", MeadSubCategory.MEAD_BASE),
    POTIONS("POTIONS", MeadSubCategory.POTION),
}