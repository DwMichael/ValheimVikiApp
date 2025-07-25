package com.rabbitv.valheimviki.data.repository.food

import com.rabbitv.valheimviki.data.local.dao.FoodDao
import com.rabbitv.valheimviki.data.remote.api.ApiFoodService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class FoodRepositoryImpl @Inject constructor(
	private val apiService: ApiFoodService,
	private val foodDao: FoodDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FoodRepository {
	override suspend fun fetchFoodList(lang: String): Response<List<Food>> {
		return withContext(ioDispatcher) {
			apiService.fetchFoodList(lang)
		}
	}

	override fun getLocalFoodList(): Flow<List<Food>> {
		return foodDao.getLocalFoodList().flowOn(ioDispatcher)
	}

	override fun getFoodListBySubCategory(subCategory: String): Flow<List<Food>> {
		return foodDao.getFoodListBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override fun getFoodListByIds(ids: List<String>): Flow<List<Food>> {
		return foodDao.getFoodListByIds(ids).flowOn(ioDispatcher)
	}

	override fun getFoodById(id: String): Flow<Food?> {
		return foodDao.getFoodById(id).flowOn(ioDispatcher)
	}

	override suspend fun insertFoodList(foodList: List<Food>) {
		check(foodList.isNotEmpty()) { "Food list cannot be empty , cannot insert ${foodList.size} food" }
		withContext(ioDispatcher) {
			foodDao.insertFood(foodList)
		}
	}


}