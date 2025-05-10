package com.rabbitv.valheimviki.presentation.detail.creature.components

import com.rabbitv.valheimviki.domain.model.material.Material

data class DropItem(
    val material: Material,
    val quantityList: List<Int?> = emptyList(),
    val chanceStarList: List<Int?> = emptyList(),
)