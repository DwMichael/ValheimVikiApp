package com.rabbitv.valheimviki.data.repository.food

import com.rabbitv.valheimviki.data.local.dao.FoodDao
import com.rabbitv.valheimviki.data.remote.api.ApiFoodService
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FoodRepositoryImpl @Inject constructor(
    private val apiService: ApiFoodService,
    private val foodDao: FoodDao,
) : FoodRepository {
    override suspend fun fetchFoodList(lang: String): Response<List<Food>> {
        return apiService.fetchFoodList(lang)
    }

    override fun getLocalFoodList(): Flow<List<Food>> {
        return foodDao.getLocalFoodList()
    }

    override fun getFoodListBySubCategory(subCategory: String): List<Food> {
        return foodDao.getFoodListBySubCategory(subCategory)
    }

    override fun getFoodListByIds(ids: List<String>): List<Food> {
        return foodDao.getFoodListByIds(ids)
    }

    override suspend fun insertFoodList(foodList: List<Food>) {
        check(foodList.isNotEmpty()) { "Food list cannot be empty , cannot insert ${foodList.size} food" }
        foodDao.insertFood(foodList)
    }


}