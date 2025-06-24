package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.presentation.DroppableType

interface Droppable {
	val itemDrop: ItemData
	val quantityList: List<Int?>
	val chanceStarList: List<Int?>
	val droppableType: DroppableType?
}

