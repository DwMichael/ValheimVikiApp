package com.rabbitv.valheimviki.presentation.detail.mead.model


import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.repository.Droppable

data class RecipeMeadData(
	override val itemDrop: Mead,
	override val quantityList: List<Int?> = emptyList(),
	override val chanceStarList: List<Int?> = emptyList(),
) : Droppable
