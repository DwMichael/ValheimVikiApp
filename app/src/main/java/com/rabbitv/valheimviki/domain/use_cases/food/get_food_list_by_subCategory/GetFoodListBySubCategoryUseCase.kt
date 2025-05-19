package com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFoodListBySubCategoryUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(subCategory: FoodSubCategory): Flow<List<Food>> {
        return foodRepository.getFoodListBySubCategory(subCategory.toString())
            .map { food -> food.sortedBy { it.order } }
    }
}