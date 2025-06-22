package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.food.Food

data class RecipeFoodData(
	val item: Food,
	val quantityList: List<Int?> = emptyList(),
	val chanceStarList: List<Int?> = emptyList(),
)
