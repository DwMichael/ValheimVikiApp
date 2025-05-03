package com.rabbitv.valheimviki.data.remote.api


import com.rabbitv.valheimviki.domain.model.food.Food
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFoodService {
    @GET("Food")
    suspend fun fetchFoodList(@Query("lang") lang: String): Response<List<Food>>
}