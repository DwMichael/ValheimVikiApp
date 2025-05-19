package com.rabbitv.valheimviki.presentation.food.model

import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class FoodSegmentOption(
    override val label: String,
    override val value: FoodSubCategory
) : SegmentedOption<FoodSubCategory> {
    COOKED("Passive", FoodSubCategory.COOKED_FOOD),
    UNCOOKED("Aggressive", FoodSubCategory.UNCOOKED_FOOD),
}