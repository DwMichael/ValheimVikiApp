package com.rabbitv.valheimviki.domain.model.food

import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable

data class FoodDrop(
	override val itemDrop: Food,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
	override val droppableType: DroppableType = DroppableType.FOOD
): Droppable
