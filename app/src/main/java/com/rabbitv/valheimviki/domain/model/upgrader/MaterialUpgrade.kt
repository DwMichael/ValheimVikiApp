package com.rabbitv.valheimviki.domain.model.upgrader

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable

data class MaterialUpgrade(
	val material: Material,
	override val quantityList: List<Int?> = emptyList(),
) : Droppable {
	override val itemDrop: Material get() = material
	override val chanceStarList: List<Int?> = emptyList()
	override val droppableType: DroppableType = DroppableType.MATERIAL
}