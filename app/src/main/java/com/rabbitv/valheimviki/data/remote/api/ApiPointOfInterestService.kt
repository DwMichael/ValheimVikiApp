package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiPointOfInterestService {
    @GET("PointOfInterests")
    suspend fun getPointsOfInterest(
        @Query("lang") lang: String
    ): Response<List<PointOfInterest>>

}