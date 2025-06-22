package com.rabbitv.valheimviki.domain.repository

interface Droppable {
	val itemDrop: ItemData
	val quantityList: List<Int?>
	val chanceStarList: List<Int?>
}