package com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids

import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFoodListByIdsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(ids: List<String>): Flow<List<Food>> =
        foodRepository.getFoodListByIds(ids).flowOn(
            Dispatchers.IO
        )
}
