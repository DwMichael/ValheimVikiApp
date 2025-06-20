package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.food.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM food")
    fun getLocalFoodList(): Flow<List<Food>>

    @Query("SELECT * FROM food where subCategory = :subCategory")
    fun getFoodListBySubCategory(subCategory: String): Flow<List<Food>>

    @Query("SELECT * FROM food where id IN (:ids)")
    fun getFoodListByIds(ids: List<String>): Flow<List<Food>>


    @Query("SELECT * FROM food where id = id")
    fun getFoodById(id: String): Flow<Food?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(foodList: List<Food>)


}