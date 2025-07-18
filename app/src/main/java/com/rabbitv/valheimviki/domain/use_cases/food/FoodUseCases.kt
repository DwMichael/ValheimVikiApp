package com.rabbitv.valheimviki.domain.use_cases.food

import com.rabbitv.valheimviki.domain.use_cases.food.get_food_by_id.GetFoodByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class FoodUseCases @Inject constructor(
	val getFoodBySubCategoryUseCase: GetFoodListBySubCategoryUseCase,
	val getFoodListByIdsUseCase: GetFoodListByIdsUseCase,
	val getFoodByIdUseCase: GetFoodByIdUseCase,
	val getLocalFoodListUseCase: GetLocalFoodListUseCase
)