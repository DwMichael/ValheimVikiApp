package com.rabbitv.valheimviki.presentation.food.model

import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class FoodSegmentOption(
    override val labelRes: Int,
    override val value: FoodSubCategory
) : SegmentedOption<FoodSubCategory> {
    COOKED(com.rabbitv.valheimviki.R.string.segment_cooked, FoodSubCategory.COOKED_FOOD),
    UNCOOKED(com.rabbitv.valheimviki.R.string.segment_uncooked, FoodSubCategory.UNCOOKED_FOOD),
}