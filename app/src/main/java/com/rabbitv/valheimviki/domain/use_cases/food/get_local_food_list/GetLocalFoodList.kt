package com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalFoodList @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<List<Food>> {
        return foodRepository.getLocalFoodList()
    }
}