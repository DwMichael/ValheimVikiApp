package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PointOfInterestRepository {

    fun getLocalPointOfInterest():Flow<List<PointOfInterest>>
    fun getPointOfInterestById(id: String): Flow<PointOfInterest?>
    fun getPointOfInterestsByIds(ids: List<String>): Flow<List<PointOfInterest>>
    fun getPointOfInterestBySubCategory(subCategory: String) : Flow<List<PointOfInterest>>
    fun getPointOfInterestBySubCategoryAndId(subCategory: String, id: String): Flow<PointOfInterest?>

    suspend fun insertPointOfInterest(pointOfInterests: List<PointOfInterest>)
    suspend fun fetchPointOfInterests(lang: String): Response<List<PointOfInterest>>
}