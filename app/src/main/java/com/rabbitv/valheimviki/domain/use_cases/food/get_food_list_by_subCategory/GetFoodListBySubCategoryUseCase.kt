package com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory

import com.rabbitv.valheimviki.domain.exceptions.FoodFetchLocalException
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodListBySubCategoryUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(subCategory: FoodSubCategory): List<Food> {
        return try {
            foodRepository.getFoodListBySubCategory(subCategory.toString())
        } catch (e: Exception) {
            throw FoodFetchLocalException("There is no food for this subcategory ")
        }
    }
}