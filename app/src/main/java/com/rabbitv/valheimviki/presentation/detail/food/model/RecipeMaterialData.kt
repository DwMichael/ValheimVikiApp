package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.utils.inferDropType

data class RecipeMaterialData<out T : ItemData>(
	override val itemDrop: T,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
	override val droppableType: DroppableType = itemDrop.inferDropType()
) : Droppable
