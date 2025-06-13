package com.rabbitv.valheimviki.domain.model.weapon

import com.rabbitv.valheimviki.domain.model.material.Material

data class MaterialUpgrade(
    val material: Material,
    val quantityList: List<Int?> = emptyList(),
)
