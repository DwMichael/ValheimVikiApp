package com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids

import com.rabbitv.valheimviki.domain.exceptions.FoodByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodListByIdsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(ids: List<String>): List<Food> {
        return try {
            val food = foodRepository.getFoodListByIds(ids)
            if (food.isNotEmpty()) {
                food.sortedBy { it.order }
            } else {
                throw FoodByIdsFetchLocalException("No food found with ids $ids")
            }
        } catch (e: Exception) {
            throw FoodByIdsFetchLocalException("Error fetching from Room food by ids : ${e.message}")
        }
    }
}
