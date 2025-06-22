package com.rabbitv.valheimviki.presentation.detail.food.model

import com.rabbitv.valheimviki.domain.model.material.Material

data class RecipeMaterialData(
	val item: Material,
	val quantityList: List<Int?> = emptyList(),
	val chanceStarList: List<Int?> = emptyList(),
)
