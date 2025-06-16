package com.rabbitv.valheimviki.domain.model.food

data class FoodAsMaterialUpgrade(
    val materialFood: Food,
    val quantityList: List<Int?> = emptyList(),
)
