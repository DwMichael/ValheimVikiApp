package com.rabbitv.valheimviki.presentation.detail.material.model

import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable

data class MaterialToCraft(
	val material: Material,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
	override val droppableType: DroppableType = DroppableType.MATERIAL
) : Droppable {
	override val itemDrop: Material get() = material
}