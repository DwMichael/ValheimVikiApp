package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.Droppable

data class RecipeFoodData(
	override val itemDrop: Food,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
) : Droppable
