package com.rabbitv.valheimviki.domain.model.material

data class MaterialDrop(
	val itemDrop: Material,
	val quantityList: List<Int?> = emptyList(),
	val chanceStarList: List<Int?> = emptyList(),
)