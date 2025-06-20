package com.rabbitv.valheimviki.domain.model.food

import kotlinx.serialization.Serializable

@Serializable
data class FoodDetail(
	val foodId: String,
	val foodCategory: String
)