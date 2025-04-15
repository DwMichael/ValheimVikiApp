package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PointOfInterestRepository {

    fun getLocalPointOfInterest():Flow<List<PointOfInterest>>
    fun getPointOfInterestById(id: String): PointOfInterest?
    fun getPointOfInterestsByIds(ids: List<String>): List<PointOfInterest>
    fun getPointOfInterestBySubCategory(subCategory: String) : List<PointOfInterest>
    fun getPointOfInterestBySubCategoryAndId(subCategory: String, id: String): PointOfInterest?

    suspend fun insertPointOfInterest(pointOfInterests: List<PointOfInterest>)
    suspend fun fetchPointOfInterests(lang: String): Response<List<PointOfInterest>>
}