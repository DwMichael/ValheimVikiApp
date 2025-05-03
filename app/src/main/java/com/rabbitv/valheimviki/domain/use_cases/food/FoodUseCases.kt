package com.rabbitv.valheimviki.domain.use_cases.food

import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodList

data class FoodUseCases(
    val getLocalFoodList: GetLocalFoodList
)