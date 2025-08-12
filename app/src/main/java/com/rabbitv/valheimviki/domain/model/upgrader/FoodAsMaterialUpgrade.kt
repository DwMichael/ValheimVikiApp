package com.rabbitv.valheimviki.domain.model.upgrader

import com.rabbitv.valheimviki.domain.model.food.Food

data class FoodAsMaterialUpgrade(
	val materialFood: Food,
	val quantityList: List<Int?> = emptyList(),
)