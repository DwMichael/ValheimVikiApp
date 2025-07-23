package com.rabbitv.valheimviki.data.repository.point_of_interest

import com.rabbitv.valheimviki.data.local.dao.PointOfInterestDao
import com.rabbitv.valheimviki.data.remote.api.ApiPointOfInterestService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class PointOfInterestRepositoryImpl @Inject constructor(
	private val apiService: ApiPointOfInterestService,
	private val pointOfInterestDao: PointOfInterestDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,

	) : PointOfInterestRepository {
	override fun getLocalPointOfInterest(): Flow<List<PointOfInterest>> {
		return pointOfInterestDao.getAllPointsOfInterest().flowOn(ioDispatcher)
	}

	override fun getPointOfInterestById(id: String): Flow<PointOfInterest?> {
		return pointOfInterestDao.getPointOfInterestById(id).flowOn(ioDispatcher)
	}

	override fun getPointOfInterestsByIds(ids: List<String>): Flow<List<PointOfInterest>> {
		return pointOfInterestDao.getPointsOfInterestByIds(ids).flowOn(ioDispatcher)
	}

	override fun getPointOfInterestBySubCategory(subCategory: String): Flow<List<PointOfInterest>> {
		return pointOfInterestDao.getPointsOfInterestBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override fun getPointOfInterestBySubCategoryAndId(
		subCategory: String,
		id: String
	): Flow<PointOfInterest?> {
		return pointOfInterestDao.getPointOfInterestBySubCategoryAndId(subCategory, id)
			.flowOn(ioDispatcher)
	}

	override suspend fun insertPointOfInterest(pointOfInterests: List<PointOfInterest>) {
		check(pointOfInterests.isNotEmpty(), { "pointOfInterests cannot be empty" })
		return withContext(ioDispatcher) {
			pointOfInterestDao.insertPointsOfInterest(pointOfInterests)
		}
	}

	override suspend fun fetchPointOfInterests(lang: String): Response<List<PointOfInterest>> {
		return withContext(ioDispatcher) { apiService.getPointsOfInterest(lang) }
	}


}