package com.rabbitv.valheimviki.domain.use_cases.food

import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase

data class FoodUseCases(
    val getFoodBySubCategoryUseCase: GetFoodListBySubCategoryUseCase,
    val getFoodListByIdsUseCase: GetFoodListByIdsUseCase,
    val getLocalFoodListUseCase: GetLocalFoodListUseCase
)