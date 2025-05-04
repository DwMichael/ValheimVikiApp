package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.food.Food
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface FoodRepository {
    suspend fun fetchFoodList(lang: String): Response<List<Food>>
    fun getLocalFoodList(): Flow<List<Food>>
    fun getFoodListBySubCategory(subCategory: String): List<Food>
    fun getFoodListByIds(ids: List<String>): List<Food>
    suspend fun insertFoodList(foodList: List<Food>)
}