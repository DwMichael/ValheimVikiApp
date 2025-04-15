package com.rabbitv.valheimviki.data.repository.point_of_interest

import com.rabbitv.valheimviki.data.local.dao.PointOfInterestDao
import com.rabbitv.valheimviki.data.remote.api.ApiPointOfInterestService
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class PointOfInterestRepositoryImpl @Inject constructor(
    private val apiService : ApiPointOfInterestService,
    private val pointOfInterestDao: PointOfInterestDao
) : PointOfInterestRepository{
    override fun getLocalPointOfInterest(): Flow<List<PointOfInterest>> {
        return pointOfInterestDao.getAllPointsOfInterest()
    }

    override fun getPointOfInterestById(id: String): PointOfInterest? {
        return pointOfInterestDao.getPointOfInterestById(id)
    }

    override fun getPointOfInterestsByIds(ids: List<String>): List<PointOfInterest> {
        return pointOfInterestDao.getPointsOfInterestByIds(ids)
    }

    override fun getPointOfInterestBySubCategory(subCategory: String): List<PointOfInterest> {
       return pointOfInterestDao.getPointsOfInterestBySubCategory(subCategory)
    }

    override fun getPointOfInterestBySubCategoryAndId(
        subCategory: String,
        id: String
    ): PointOfInterest? {
        return pointOfInterestDao.getPointOfInterestBySubCategoryAndId(subCategory, id)
    }

    override suspend fun insertPointOfInterest(pointOfInterests: List<PointOfInterest>) {
        check(pointOfInterests.isNotEmpty(),{"pointOfInterests cannot be empty"})
        pointOfInterestDao.insertPointsOfInterest(pointOfInterests)
    }

    override suspend fun fetchPointOfInterests(lang: String): Response<List<PointOfInterest>> {
        return  apiService.getPointsOfInterest(lang)
    }


}