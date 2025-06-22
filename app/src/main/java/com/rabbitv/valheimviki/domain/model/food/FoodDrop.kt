package com.rabbitv.valheimviki.domain.model.food

data class FoodDrop(
	val itemDrop: Food,
	val quantityList: List<Int?> = emptyList(),
	val chanceStarList: List<Int?> = emptyList(),
)
