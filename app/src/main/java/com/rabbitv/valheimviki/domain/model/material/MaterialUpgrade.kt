package com.rabbitv.valheimviki.domain.model.material

data class MaterialUpgrade(
    val material: Material,
    val quantityList: List<Int?> = emptyList(),
)
