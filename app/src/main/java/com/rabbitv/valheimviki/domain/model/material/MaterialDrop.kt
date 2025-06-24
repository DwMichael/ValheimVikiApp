package com.rabbitv.valheimviki.domain.model.material

import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable

data class MaterialDrop(
	override val itemDrop: Material,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
	override val droppableType: DroppableType = DroppableType.MATERIAL
) : Droppable