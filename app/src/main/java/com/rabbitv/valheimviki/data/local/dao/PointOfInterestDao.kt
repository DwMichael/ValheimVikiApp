package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import kotlinx.coroutines.flow.Flow

@Dao
interface PointOfInterestDao {
    @Query("SELECT * FROM point_of_interest")
    fun getAllPointsOfInterest(): Flow<List<PointOfInterest>>

    @Query("SELECT * FROM point_of_interest WHERE id = :id")
    fun getPointOfInterestById(id: String): PointOfInterest?

    @Query("SELECT * FROM point_of_interest WHERE id IN (:ids)")
    fun getPointsOfInterestByIds(ids:List<String>): List<PointOfInterest>

    @Query("SELECT * FROM point_of_interest WHERE subCategory = :subCategory")
    fun getPointsOfInterestBySubCategory(subCategory: String): List<PointOfInterest>

    @Query("SELECT * FROM point_of_interest WHERE subCategory = :subCategory and id = :id")
    fun getPointOfInterestBySubCategoryAndId(subCategory: String,id:String): PointOfInterest?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointsOfInterest(pointOfInterests:List<PointOfInterest>)

}