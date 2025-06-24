package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable

data class RecipeMaterialData(
	override val itemDrop: Material,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
	override val droppableType: DroppableType = DroppableType.MATERIAL
) : Droppable
