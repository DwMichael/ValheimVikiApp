package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class MaterialSegmentOption(
    override val label: String,
    override val value: MaterialSubCategory
) : SegmentedOption<MaterialSubCategory> {
    CREATURE_DROP("CREATURE DROP", MaterialSubCategory.CREATURE_DROP),
}