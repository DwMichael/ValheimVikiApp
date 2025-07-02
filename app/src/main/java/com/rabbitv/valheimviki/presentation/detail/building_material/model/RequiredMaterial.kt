package com.rabbitv.valheimviki.presentation.detail.building_material.model

import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.domain.repository.ItemData

data class RequiredMaterial(
	override val itemDrop: ItemData,
	override val quantityList: List<Int?>,
	override val chanceStarList: List<Int?>,
	override val droppableType: DroppableType?
) : Droppable

