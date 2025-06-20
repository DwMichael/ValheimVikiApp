package com.rabbitv.valheimviki.domain.use_cases.food.get_food_by_id

import com.rabbitv.valheimviki.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodByIdUseCase @Inject constructor(
    private val foodRepository: FoodRepository
){
    operator  fun invoke(id: String) = foodRepository.getFoodById(id)
}